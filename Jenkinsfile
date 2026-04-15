pipeline {
    agent any //{ label 'slave1' }

    tools {
        maven 'm3' 
        jdk 'jdk21'
    }

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
                // Debugging line: Escaped \$ ensures the shell runs the command
                sh 'echo "Running as User: $(whoami)"'
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
                sh "docker rm -f ${IMAGE_NAME} || true"
                sh "docker run -d --name ${IMAGE_NAME} -p 8081:8081 ${DOCKER_HUB_USER}/${IMAGE_NAME}:${TAG}"
            }
        }
    }

    post {
        success {
            echo "Pipeline successful! Cleaning up local images..."
            // Cleaning up the slave to prevent disk space issues
            sh "docker rmi ${IMAGE_NAME}:${TAG} ${DOCKER_HUB_USER}/${IMAGE_NAME}:${TAG} || true"
        }
        failure {
            // This block runs ONLY if the build fails at any stage
            echo "Pipeline FAILED! Sending notification..."
            sh 'echo "Failure detected by user: $(whoami)"'
            // You could add email or Slack notifications here
        }
        always {
            // Final debug check to see who finished the job
            sh 'echo "Job finished by user: $(whoami)"'
        }
    }
}
