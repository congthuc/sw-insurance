pipeline {
    agent any
    environment {
        // Application configuration
        APP_NAME = 'sw-insurance'
        DOCKER_IMAGE = "your-docker-registry/${APP_NAME}:${env.BUILD_NUMBER}"
        
        // SonarQube configuration (uncomment if using SonarQube)
        // SONAR_SCANNER_HOME = tool 'SonarQubeScanner'
        
        // Slack notification channel (uncomment if using Slack)
        // SLACK_CHANNEL = '#your-channel'
    }
    
    tools {
        // Define tools versions
        maven 'Maven 3.9.0'
        jdk 'jdk-17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    // Store Git commit hash for later use
                    env.GIT_COMMIT_HASH = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                }
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    // Uncomment for test coverage reports
                    // publishHTML target: [
                    //     allowMissing: false,
                    //     alwaysLinkToLastBuild: true,
                    //     keepAll: true,
                    //     reportDir: 'target/site/jacoco',
                    //     reportFiles: 'index.html',
                    //     reportName: 'Code Coverage Report'
                    // ]
                }
            }
        }
        
        stage('Code Quality') {
            steps {
                // Uncomment if using SonarQube
                // withSonarQubeEnv('SonarQube') {
                //     sh 'mvn sonar:sonar'
                // }
                
                // Checkstyle
                sh 'mvn checkstyle:checkstyle'
            }
            post {
                // Uncomment if using SonarQube
                // success {
                //     timeout(time: 1, unit: 'HOURS') {
                //         waitForQualityGate abortPipeline: true
                //     }
                // }
            }
        }
        
        stage('Build Docker Image') {
            when {
                // Only build Docker image for main branch or tags
                anyOf {
                    branch 'main'
                    tag '*'
                }
            }
            steps {
                script {
                    docker.withRegistry('https://your-docker-registry', 'docker-hub-credentials') {
                        def customImage = docker.build(DOCKER_IMAGE)
                        customImage.push()
                        // Push with 'latest' tag for main branch
                        if (env.BRANCH_NAME == 'main') {
                            customImage.push('latest')
                        }
                    }
                }
            }
        }
        
        stage('Deploy to Staging') {
            when {
                branch 'main'
            }
            steps {
                // Example: Deploy to Kubernetes
                // sh "kubectl set image deployment/${APP_NAME} ${APP_NAME}=${DOCKER_IMAGE} -n staging"
                
                // Or use your deployment script
                // sh "./deploy.sh staging ${DOCKER_IMAGE}"
                
                echo "Deployed ${DOCKER_IMAGE} to staging environment"
            }
        }
        
        stage('Deploy to Production') {
            when {
                tag '*'
            }
            steps {
                // Example: Deploy to Kubernetes
                // sh "kubectl set image deployment/${APP_NAME} ${APP_NAME}=${DOCKER_IMAGE} -n production"
                
                // Or use your deployment script
                // sh "./deploy.sh production ${DOCKER_IMAGE}"
                
                echo "Deployed ${DOCKER_IMAGE} to production environment"
            }
        }
    }
    
    post {
        always {
            // Clean up workspace
            cleanWs()
        }
        success {
            // Uncomment if using Slack
            // slackSend(
            //     channel: SLACK_CHANNEL,
            //     color: 'good',
            //     message: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
            // )
        }
        failure {
            // Uncomment if using Slack
            // slackSend(
            //     channel: SLACK_CHANNEL,
            //     color: 'danger',
            //     message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
            // )
        }
    }
}
