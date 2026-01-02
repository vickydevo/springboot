pipeline{
          agent any
        tools {
            maven 'maven-3'
            jdk 'jdk-17'
            // git 'git-2'  not to use this in tools block
            // dockerTool 'docker' not to use this in tools block
        }
        parameters {
            string(name: 'ImageName', defaultValue: 'springboot-hello', description: 'Name of the Docker Image')
            string(name: 'ImageTag', defaultValue: '1.0', description: 'Name of the Docker Image Tag')
        }

        environment{
            IMAGE_NAME = "${params.ImageName}"
            TAG = "${params.ImageTag}"
            // SONAR_HOST_URL = 'http://ec2-18-215-4-93.compute-1.amazonaws.com:9000'
           // SONAR_AUTH_TOKEN = credentials('squ_7177b164d811d4441d58b34a507d806a31a')
        }

    
        stages {
            stage ("SCM-checkout") {
                steps{
                    git branch: 'main', url: 'https://github.com/vickydevo/springboot-hello.git'
                }
            }
            stage ("Build-Artifact") {
                steps{
                   sh 'mvn clean package'
                }
            }// stage2
            stage ("DockerImage"){
                steps{

                sh "docker build -t ${IMAGE_NAME}:${TAG} ."
                }
            }// stage3
            stage ("DockerPush") {
                steps{
                        withCredentials([usernamePassword(
                            credentialsId: 'docker-cred',
                            usernameVariable: 'DOCKERUSER',
                            passwordVariable: 'DOCKER_PASSWORD')]) {
                   sh """ 
         echo "${DOCKER_PASSWORD}"  |  docker login -u ${DOCKERUSER} --password-stdin
                    docker tag ${IMAGE_NAME}:${TAG} ${DOCKERUSER}/${IMAGE_NAME}:${TAG}
                    docker push ${DOCKERUSER}/${IMAGE_NAME}:${TAG} 
                    """
                            }
                }
            }// stage4
            // stage ("Deploy") {}
        }
}
