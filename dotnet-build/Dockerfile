#.NET Core SDK image with Azure DevOps Artifact Credential Provider
FROM mcr.microsoft.com/dotnet/core/sdk:2.2
# Install the Credential Provider
RUN wget -qO- https://raw.githubusercontent.com/Microsoft/artifacts-credprovider/master/helpers/installcredprovider.sh | bash
# Configure environment variables 
ENV NUGET_CREDENTIALPROVIDER_SESSIONTOKENCACHE_ENABLED true
