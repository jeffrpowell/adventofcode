package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Day23 extends Solution2019<List<BigInteger>>
{
	@Override
	public int getDay()
	{
		return 23;
	}

	@Override
	public InputParser<List<BigInteger>> getInputParser()
	{
		return InputParserFactory.getBigIntegerCSVParser();
	}

	@Override
	protected String part1(List<List<BigInteger>> input)
	{
		Network network = new Network(input.get(0));
		return network.getYForPacketTo255().toString();
	}

	@Override
	protected String part2(List<List<BigInteger>> input)
	{
		return part1(input);
	}
	
	private static class Network {
		private final List<NICComputer> computers;
		private final Queue<Packet> packetQueue;
		private final ExecutorService executor;
		private Packet NAT;
		private BigInteger lastNATY;
		
		public Network(List<BigInteger> tape) {
			this.computers = new ArrayList<>();
			this.packetQueue = new ConcurrentLinkedQueue<>();
			this.executor = Executors.newFixedThreadPool(100);
			this.lastNATY = null;
			for (int i = 0; i < 50; i++)
			{
				computers.add(new NICComputer(BigInteger.valueOf(i), tape, packetQueue, executor));
			}
			executor.submit(() -> {
				while(true) {
					int idleComps = 0;
					for (NICComputer computer : computers)
					{
						if (computer.inputQueue.isEmpty()) {
							idleComps++;
							computer.inputQueue.put(BigInteger.valueOf(-1));
						}
					}
					if (idleComps == 50 && !computers.get(0).inputQueue.contains(BigInteger.valueOf(-1))) {
						if (lastNATY != null && lastNATY.equals(NAT.y)) {
							System.out.println("WINNER!!!!!!!!!     " + NAT.y);
						}
						lastNATY = NAT.y;
						computers.get(0).dispatchPacket(NAT);
					}
				}
			});
			executor.submit(() -> {
				while(true) {
					for (NICComputer computer : computers)
					{
						if (!computer.outputQueue.isEmpty()) {
							Packet packet = new Packet(computer.outputQueue.take(), computer.outputQueue.take(), computer.outputQueue.take());
							System.out.println(packet.address + ": ("+packet.x+","+packet.y+")");
							if (packet.address.equals(BigInteger.valueOf(255))) {
								NAT = packet;
							}
							else {
								computers.get(packet.address.intValue()).dispatchPacket(packet);
							}
						}
					}
				}
			});
		}
		
		public BigInteger getYForPacketTo255() {
			return computers.get(49).getYForPacketTo255();
		}
	}
	
	private static class Packet {
		private final BigInteger address;
		private final BigInteger x;
		private final BigInteger y;

		public Packet(BigInteger address, BigInteger x, BigInteger y)
		{
			this.address = address;
			this.x = x;
			this.y = y;
		}
		
	}
	
	private static class NICComputer {
		private final BigInteger address;
		private final Queue<Packet> packetQueue;
		private final IntCodeComputer computer;
		private final BlockingQueue<BigInteger> inputQueue;
		private final BlockingQueue<BigInteger> outputQueue;
		private final CountDownLatch countdown255;
		private BigInteger y255;

		public NICComputer(BigInteger address, List<BigInteger> tape, Queue<Packet> packetQueue, ExecutorService executor)
		{
			this.address = address;
			this.packetQueue = packetQueue;
			this.inputQueue = IntCodeComputer.generateDefaultBlockingQueue(address);
			this.outputQueue = IntCodeComputer.generateDefaultBlockingQueue();
			this.computer = new IntCodeComputer(tape, inputQueue, outputQueue);
			this.countdown255 = new CountDownLatch(1);
			executor.submit(computer::executeProgram);
		}
		
		public BigInteger getYForPacketTo255() {
			try
			{
				countdown255.await();
			}
			catch (InterruptedException ex)
			{
			}
			return y255;
		}
		
		public void dispatchPacket(Packet packet) {
			try
			{
				inputQueue.put(packet.x);
				inputQueue.put(packet.y);
			}
			catch (InterruptedException ex)
			{
			}
			if (packet.address.equals(BigInteger.valueOf(255))) {
				y255 = packet.y;
				countdown255.countDown();
			}
		}
	}
}
