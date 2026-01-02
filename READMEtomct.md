# Create a Tomcat User: good practice to run Tomcat as a dedicated user. Create a user for Tomcat:

sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat
# Install Tomcat: Download and install Apache Tomcat. You can get the latest version from the official Tomcat website. For example, if you download and extract Tomcat to /opt/tomcat, the following commands can be used:

cd /tmp
wget https://downloads.apache.org/tomcat/tomcat-9/v9.0.59/bin/apache-tomcat-9.0.59.tar.gz
sudo tar -xf apache-tomcat-9.0.59.tar.gz -C /opt/tomcat
# Create a Systemd Service File: Create a systemd service file for Tomcat:

sudo vi /etc/systemd/system/tomcat.service
# Edit the Service File: Add the following content to the tomcat.service file:
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking
User=tomcat
Group=tomcat

Environment="JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
Environment="CATALINA_BASE=/opt/tomcat"
Environment="CATALINA_HOME=/opt/tomcat"
Environment="CATALINA_PID=/opt/tomcat/temp/tomcat.pid"
Environment="CATALINA_OPTS="
ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh
SuccessExitStatus=143
StandardOutput=null

[Install]
WantedBy=multi-user.target

# Adjust Permissions: Ensure that the Tomcat installation directory is owned by the tomcat user
sudo chown -R tomcat:tomcat /opt/tomcat

# Reload Systemd and Start Tomcat
sudo systemctl daemon-reload
sudo systemctl start tomcat

# Enable Autostart (Optional): If you want Tomcat to start automatically on system boot, enable the service:

sudo systemctl enable tomcat

# Now Tomcat should be running as a systemd service on your Ubuntu EC2 instance. You can manage it using systemctl commands like start, stop, restart, and status



 