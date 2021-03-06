FROM buildpack-deps:bionic-scm

# To make it easier for build and release pipelines to run apt-get,
# configure apt to not require confirmation (assume the -y argument by default)
ENV DEBIAN_FRONTEND=noninteractive
RUN echo "APT::Get::Assume-Yes \"true\";" > /etc/apt/apt.conf.d/90assumeyes

RUN apt-get update \
&& apt-get install -y --no-install-recommends \
        jq \
        git \
        iputils-ping \
        libicu60 \
        libunwind8 \
        netcat

#Install docker 18.09.7
ENV DOCKER_BUCKET download.docker.com
ENV DOCKER_VERSION 18.09.7
# TODO: Verify sha256
RUN set -x \
	&& curl -fSL "https://${DOCKER_BUCKET}/linux/static/stable/x86_64/docker-${DOCKER_VERSION}.tgz" -o docker.tgz \
	&& tar -xzvf docker.tgz \
    && mv docker/* /usr/bin \
    && chmod +x /usr/bin/docker \
	&& rm docker.tgz \
    && rm -r docker \
	&& docker -v

# Install dotnet core 3.1 SDK
ENV \
    # Enable detection of running in a container
    DOTNET_RUNNING_IN_CONTAINER=true \
    # Enable correct mode for dotnet watch (only mode supported in a container)
    DOTNET_USE_POLLING_FILE_WATCHER=true \
    # Skip extraction of XML docs - generally not useful within an image/container - helps performance
    NUGET_XMLDOC_MODE=skip \
    # PowerShell telemetry for docker image usage
    POWERSHELL_DISTRIBUTION_CHANNEL=PSDocker-DotnetCoreSDK-Ubuntu-18.04

# Install .NET CLI dependencies
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        libc6 \
        libgcc1 \
        libgssapi-krb5-2 \
        libicu60 \
        libssl1.1 \
        libstdc++6 \
        zlib1g \
    && rm -rf /var/lib/apt/lists/*

# Install .NET Core SDK 3.1
RUN dotnet_sdk_version=3.1.101 \
    && curl -SL --output dotnet.tar.gz https://dotnetcli.azureedge.net/dotnet/Sdk/$dotnet_sdk_version/dotnet-sdk-$dotnet_sdk_version-linux-x64.tar.gz \
    && dotnet_sha512='eeee75323be762c329176d5856ec2ecfd16f06607965614df006730ed648a5b5d12ac7fd1942fe37cfc97e3013e796ef278e7c7bc4f32b8680585c4884a8a6a1' \
    && echo "$dotnet_sha512 dotnet.tar.gz" | sha512sum -c - \
    && mkdir -p /usr/share/dotnet \
    && tar -ozxf dotnet.tar.gz -C /usr/share/dotnet \
    && rm dotnet.tar.gz \
    && ln -s /usr/share/dotnet/dotnet /usr/bin/dotnet \
    # Trigger first run experience by running arbitrary cmd
    && dotnet help

# Add .NET tools to PATH
ENV PATH="/root/.dotnet/tools:${PATH}"
# Install Cake 0.36 as a dotnet global tool
RUN dotnet tool install -g Cake.Tool --version 0.36.0
# Install GitVersion 5.1.3 as a dotnet global tool
RUN dotnet tool install -g GitVersion.Tool --version 5.1.3
# Install Reportgenerator 4.4.6 as a dotnet global tool
RUN dotnet tool install --global dotnet-reportgenerator-globaltool --version 4.4.6


# Install kubectl
ENV KUBE_VERSION="v1.3.5"
RUN set -x \
    && curl -L https://storage.googleapis.com/kubernetes-release/release/${KUBE_VERSION}/bin/linux/amd64/kubectl -o /usr/bin/kubectl \
    && chmod +x /usr/bin/kubectl

# Install mono
ENV MONO_VERSION=6.8.0.96
RUN apt-get update \
    && apt-get install -y --no-install-recommends gnupg dirmngr \
    && rm -rf /var/lib/apt/lists/* \
    && export GNUPGHOME="$(mktemp -d)" \
    && gpg --batch --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 3FA7E0328081BFF6A14DA29AA6A19B38D3D831EF \
    && gpg --batch --export --armor 3FA7E0328081BFF6A14DA29AA6A19B38D3D831EF > /etc/apt/trusted.gpg.d/mono.gpg.asc \
    && gpgconf --kill all \
    && rm -rf "$GNUPGHOME" \
    && apt-key list | grep Xamarin \
    && apt-get purge -y --auto-remove gnupg dirmngr

RUN echo "deb http://download.mono-project.com/repo/ubuntu stable-bionic/snapshots/$MONO_VERSION main" > /etc/apt/sources.list.d/mono-official-vs.list \
    && apt-get update \
    && apt-get install -y mono-runtime \
    && rm -rf /var/lib/apt/lists/* /tmp/* \
    && apt-get update \
    && apt-get install -y binutils curl mono-devel ca-certificates-mono fsharp mono-vbnc nuget referenceassemblies-pcl \
    && rm -rf /var/lib/apt/lists/* /tmp/* \
    && mono --version

# Copy agent start
WORKDIR /azp
COPY ./start.sh .
RUN chmod +x start.sh

CMD ["./start.sh"]
