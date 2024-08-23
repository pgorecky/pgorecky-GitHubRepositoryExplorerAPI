# GitHub Repository Explorer API
GitHub Repository Explorer API is an application developed using Java 21 and Spring Framework. The application allows to retrieve and process user's repositories from GitHub that are not forks and get detailed information about repository branches.

## Requirements
- Java 21
- Gradle
- Internet access

## How to install and run the application
1. Clone the repository:

    ```bash
    git clone https://github.com/pgorecky/pgorecky-GitHubRepositoryExplorerAPI.git
    ```

2. Go to the project directory:

    ```bash
    cd pgorecky-GitHubRepositoryExplorerAPI
    ```

3. Build a project using Gradle:
    ```bash
    ./gradlew build
    ```

4. Run application using Gradle 
    ```bash
    ./gradlew bootRun
    ```

5. The application will be available at: `http://localhost:8080`

## How to use the application
After proper launching, the application is ready for use.
An API consumer can send a `GET` request to the endpoint `/api/github/users/${username}` where username is the name of the user about whom we want to get information

### Example
```bash
curl -X GET "http://localhost:8080/api/github/users/pgorecky"
```

### The response returned for the sample request
```json
[
    {
        "name": "pgorecky-GitHubRepositoryExplorerAPI",
        "owner": {
            "login": "pgorecky"
        },
        "branches": [
            {
                "name": "main",
                "commit": {
                    "sha": "6d68bec34beea536c404adb1d10ba674680fda1d"
                }
            }
        ]
    },
    {
        "name": "WorldlyEats",
        "owner": {
            "login": "pgorecky"
        },
        "branches": [
            {
                "name": "main",
                "commit": {
                    "sha": "c9bf74c823a5673c6bc3b8453889ba721def7ded"
                }
            }
        ]
    }
]
```

## Response scheme
### Response for existing user
```json
[
    {
        "name": "${repositoryName}",
        "owner": {
            "login": "${ownerLogin}"
        },
        "branches": [
            {
                "name": "${branchName}",
                "commit": {
                    "sha": "${lastCommitSha}"
                }
            }
        ]
    }
]
```

### Response for non existing user
```json
{
    "status": 404,
    "message": "User: ${username} does not exist"
}
```
