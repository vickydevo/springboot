pipeline {
    agent {
        label 'slave1'
    }

    tools {
        maven 'm3'           // Preconfigured Maven installation in Jenkins
        jdk 'jdk17'          // Preconfigured JDK 17
    }

    parameters {
        string(name: 'image', defaultValue: 'boot-test', description: 'Enter Docker image name')
        string(name: 'tag', defaultValue: '8081', description: 'Enter Docker image TAG')
    }

    environment {
        DOCKER_IMAGE = "${params.image}"
        DOCKER_TAG = "${params.tag}"
    }

    stages {
        // stage('SCM Checkout') {
        //     steps {
        //         git branch: 'main', url: 'https://github.com/vickydevo/springboot-hello.git'
        //     }
        // }

        // stage('SonarQube Analysis') {
        //     steps {
        //         withSonarQubeEnv('SonarQube') {
        //             sh """
        //                 mvn clean verify
        //                 mvn sonar:sonar \
        //                     -Dsonar.projectKey=${DOCKER_IMAGE} \
        //                     -Dsonar.projectName=${DOCKER_IMAGE} \
        //                     -Dsonar.sources=src/main/java \
        //                     -Dsonar.tests=src/test/java \
        //                     -Dsonar.java.binaries=target/classes \
        //                     -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
        //             """
        //         }
        //     }
        // }

        // stage('OWASP Dependency Check') {
        //     steps {
        //         dependencyCheck additionalArguments: "--scan ./ --format XML --out dependency-check-report.xml", odcInstallation: 'OWASP-DC'
        //         dependencyCheckPublisher pattern: "**/dependency-check-report.xml"
        //     }
        // }

        // stage('Sonar Quality Gate') {
        //     steps {
        //         timeout(time: 2, unit: "MINUTES") {
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }
        // }

        stage('Trivy File System Scan') {
            steps {
                sh "trivy fs --format table -o trivy-fs-report.html ."
            }
        }

        stage('Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE:$DOCKER_TAG .'
            }
        }

        stage('Trivy Image Scan') {
            steps {
                sh "trivy image --format table -o trivy-image-report.html $DOCKER_IMAGE:$DOCKER_TAG"
            }
        }

        stage('DockerHub Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker_cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker tag $DOCKER_IMAGE:$DOCKER_TAG $DOCKER_USER/$DOCKER_IMAGE:$DOCKER_TAG
                        docker push $DOCKER_USER/$DOCKER_IMAGE:$DOCKER_TAG
                    '''
                }
            }
        }

        stage('Deploy App to Container') {
            steps {
                sh '''
                    docker run -d -p 8082:8081 --name demo-container $DOCKER_IMAGE:$DOCKER_TAG
                    echo "######################################"
                    echo "DOCKER CONTAINER IS RUNNING ON PORT 8081"
                    echo "######################################"
                '''
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/*.html, **/dependency-check-report.xml', allowEmptyArchive: true
        }
    }
}
