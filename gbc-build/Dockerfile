#.NET Core SDK image with Azure DevOps Artifact Credential Provider
FROM mcr.microsoft.com/dotnet/core/sdk:3.1

# Install gbc tool dependencies
RUN apt-get update || apt-get install -y \
  clang \
  cmake \
  zlib1g-dev \ 
  libboost-dev \
  libboost-thread-dev
# Install haskell platform
RUN curl -sSL https://get.haskellstack.org/ | sh

# Download sources and build
RUN git clone --recursive https://github.com/microsoft/bond.git /tmp/bond \
    && mkdir /tmp/bond/build \
    && cd /tmp/bond/build \
    && cmake -DBOND_ENABLE_GRPC=FALSE .. \
    && make \
    || mv /tmp/bond/build/compiler/build/gbc/gbc /usr/bin/gbc \
    && gbc --version
