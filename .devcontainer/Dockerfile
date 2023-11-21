FROM mcr.microsoft.com/devcontainers/java:1-21-bookworm

# Establish .m2 folder, set ownership, and copy settings.xml file to the container
RUN mkdir -p /usr/local/.m2/repository 
RUN chown -R vscode:vscode /usr/local/.m2
COPY mvnsettings.xml /usr/local/.m2/mvnsettings.xml