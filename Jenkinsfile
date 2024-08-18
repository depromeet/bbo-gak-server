pipeline {
    agent any

    tools {
        jdk 'JDK21'
    }

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials-id')
        DOCKER_IMAGE = 'jeongsangbyuk/bbogak-dev:latest'
        K8S_URL = credentials('k8s-url')
        K8S_NAMESPACE = 'bbogak-api'
        JAVA_HOME = "${tool 'JDK21'}"
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    sh './gradlew clean build'
                    sh './gradlew openapi3'
                }
            }
        }

        stage('Login'){
            steps{
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin' // docker hub 로그인
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {
                    sh 'docker buildx build --push --platform linux/amd64 -t $DOCKER_IMAGE .'
                }
            }
        }
        stage('ssh-test') {
            steps{
                script{
                    sshagent (credentials: ['ncp-key']) {
                        sh  """
                        ssh -o StrictHostKeyChecking=no ${K8S_URL} << EOF
                        microk8s kubectl rollout restart deploy deploy-bbogak-api-dev -n=$K8S_NAMESPACE
                        """
                    }
                }
            }
        }

    }

    post {
        success {
            echo 'Deployment was successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}
