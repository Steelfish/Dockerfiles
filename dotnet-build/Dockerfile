#.NET Core SDK image with Azure DevOps Artifact Credential Provider
FROM mcr.microsoft.com/dotnet/core/sdk:3.1
# Install the Credential Provider
RUN wget -qO- https://raw.githubusercontent.com/Microsoft/artifacts-credprovider/master/helpers/installcredprovider.sh | bash
# Configure environment variables 
ENV NUGET_CREDENTIALPROVIDER_SESSIONTOKENCACHE_ENABLED true

# Add gbc executable for .bond file code generation | see gbc-build
ADD gbc /usr/bin

# Add .NET tools to PATH
ENV PATH="/root/.dotnet/tools:${PATH}"
# Install Reportgenerator 4.4.6 as a dotnet global tool
RUN dotnet tool install --global dotnet-reportgenerator-globaltool --version 4.4.6