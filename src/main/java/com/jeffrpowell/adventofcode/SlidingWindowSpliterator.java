package com.jeffrpowell.adventofcode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

//https://dzone.com/articles/implementing-a-sliding-window-streamspliterator-in
public class SlidingWindowSpliterator<T> implements Spliterator<Stream<T>> {
    public static <T> Stream<Stream<T>> windowed(Collection<T> stream, int windowSize) {
        return StreamSupport.stream(
            new SlidingWindowSpliterator<>(stream, windowSize), false);
    }

    public static <T> Stream<Stream<T>> windowed(Collection<T> stream, int windowSize, T pad) {
        return StreamSupport.stream(
            new SlidingWindowSpliterator<>(stream, windowSize, pad), false);
    }
    private final Queue<T> buffer;
    private final Iterator<T> sourceIterator;
    private final int windowSize;
    private final int size;
    private final T pad;
    private int rightPadCountdown;

    private SlidingWindowSpliterator(Collection<T> source, int windowSize) {
        this.buffer = new ArrayDeque<>(windowSize);
        this.sourceIterator = Objects.requireNonNull(source).iterator();
        this.windowSize = windowSize;
        this.size = calculateSize(source, windowSize);
        this.pad = null;
        this.rightPadCountdown = 0;
    }

    private SlidingWindowSpliterator(Collection<T> source, int windowSize, T pad) {
        this.buffer = new ArrayDeque<>(windowSize);
        this.sourceIterator = Objects.requireNonNull(source).iterator();
        this.windowSize = windowSize;
        this.size = calculateSize(source, windowSize);
        this.pad = pad;
        while (this.buffer.size() < windowSize - 1) {
            this.buffer.add(pad);
        }
        rightPadCountdown = windowSize - 1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean tryAdvance(Consumer<? super Stream<T>> action) {
        if (windowSize < 1) {
            return false;
        }
        while (sourceIterator.hasNext()) {
            buffer.add(sourceIterator.next());
            if (buffer.size() == windowSize) {
                action.accept(Arrays.stream((T[]) buffer.toArray(new Object[0])));
                buffer.poll();
                return sourceIterator.hasNext() || rightPadCountdown > 0;
            }
        }
        if (rightPadCountdown > 0) {
            buffer.add(pad);
            action.accept(Arrays.stream((T[]) buffer.toArray(new Object[0])));
            buffer.poll();
            rightPadCountdown--;
            return rightPadCountdown > 0;
        }
        return false;
    }

    @Override
    public Spliterator<Stream<T>> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return size;
    }

    @Override
    public int characteristics() {
        return ORDERED | NONNULL | SIZED;
    }

    private static int calculateSize(Collection<?> source, int windowSize) {
        return source.size() < windowSize
               ? 0
               : source.size() - windowSize + 1;
    }
}
