pipeline {
    agent any

    parameters {
        string(name: 'ImageName', defaultValue: 'springboot', description: 'Name of the Docker Image')
        string(name: 'ImageTag', defaultValue: '1.0', description: 'Docker Image Tag')
    }

    environment {
        IMAGE_NAME = "${params.ImageName}"
        TAG = "${params.ImageTag}"
        DOCKER_HUB_USER = "vignan91" 
    }

    stages {
        stage("SCM-checkout") {
            steps {
                git branch: 'main', url: 'https://github.com/vickydevo/springboot.git'
            }
        }

        stage("Build-Artifact") {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage("DockerImage") {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${TAG} ."
            }
        }

        stage("DockerPush") {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-cred',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS')]) {
                        sh """ 
                        echo "${DOCKER_PASS}" | docker login -u ${DOCKER_USER} --password-stdin
                        docker tag ${IMAGE_NAME}:${TAG} ${DOCKER_USER}/${IMAGE_NAME}:${TAG}
                        docker push ${DOCKER_USER}/${IMAGE_NAME}:${TAG} 
                        """
                }
            }
        }

        stage("Deploy") {
            steps {
                // 'true' ensures the pipeline doesn't fail if the container doesn't exist yet
                sh "docker rm -f ${IMAGE_NAME} || true"
                sh "docker run -d --name ${IMAGE_NAME} -p 8081:8081 ${DOCKER_HUB_USER}/${IMAGE_NAME}:${TAG}"
            }
        }
    }
}
