#!/bin/bash
#NOTE THIS IS ASSUMING YOU ARE RUNNING AS ROOT
#update repositories first
printf "Updating and upgrading...\n"
apt-get -y update &> /dev/null 2>&1;
apt-get -y upgrade &> /dev/null 2>&1;
printf "Done."

#install the lamp stack
#user must enter the root password for MYSQL when prompted
#user must choose apache2 during phpmyadmin process 
#turn off front end promps
export DEBIAN_FRONTEND="noninteractive"
#set passwords for MySQL to use
printf "\nPlease enter MySQL root password:"
read -s mysql_passwd
printf "\nPlease enter phpmyadmin app password:"
read -s phpmyadmin_passwd
printf "\nPlease enter phpmyadmin admin password:"
read -s phpmyadmin_admin_passwd
printf "\nPlease enter phpmyadmin app-db password:"
read -s phpmyadmin_appdb_passwd
debconf-set-selections <<< "mysql-server mysql-server/root_password password $mysql_passwd"
debconf-set-selections <<< "mysql-server mysql-server/root_password_again password $mysql_passwd"
debconf-set-selections <<< "phpmyadmin phpmyadmin/dbconfig-install boolean true"
debconf-set-selections <<< "phpmyadmin phpmyadmin/reconfigure-webserver multiselect apache2"
debconf-set-selections <<< "phpmyadmin phpmyadmin/mysql/app-pass password $phpmyadmin_appdb_passwd"
debconf-set-selections <<< "phpmyadmin phpmyadmin/app-password-confirm password $phpmyadmin_passwd"
debconf-set-selections <<< "phpmyadmin phpmyadmin/mysql/admin-pass password $phpmyadmin_admin_passwd"

printf "\nInstalling LAMP Server...\n";
apt-get -y install tasksel &> /dev/null 2>&1;
apt-get -y install lamp-server^ &> /dev/null 2>&1;
apt-get -y install phpmyadmin &> /dev/null 2>&1;
printf "Done installing LAMP Server.\n";
prtinf "Begin recovery/installation of website...\n"
#clear password data to make sure variables are no longer stored
( (
clearpasswd=""
debconf-set-selections <<< "mysql-server mysql-server/root_password password $clearpasswd"
debconf-set-selections <<< "mysql-server mysql-server/root_password_again password $clearpasswd"
debconf-set-selections <<< "phpmyadmin phpmyadmin/dbconfig-install boolean true"
debconf-set-selections <<< "phpmyadmin phpmyadmin/reconfigure-webserver multiselect apache2"
debconf-set-selections <<< "phpmyadmin phpmyadmin/mysql/app-pass password $clearpasswd"
debconf-set-selections <<< "phpmyadmin phpmyadmin/app-password-confirm password $clearpasswd"
debconf-set-selections <<< "phpmyadmin phpmyadmin/mysql/admin-pass password $clearpasswd"
) & )
#upack website backup
printf "Unpacking website backup files...\n"
tar -xzf nov_28.tar.gz -C /var/www/ &> /dev/null 2>&1;
chown -R www-data:www-data /var/www/;
printf "Done unpacking website backup.\n";
printf "Creating log folder...\n";
mkdir -p /var/log/comp424_website/;
chown www-data:www-data /var/log/comp424_website/;
printf "Done creating log folder.\n";

#import database
#the password must be entered when prompted for it for security reasons
#for easy of use, only need to enter the password once
printf "Importing backup database...\n"
mysql -u root -p$mysqlpasswd < fulldump.sql &> /dev/null 2>&1;
mysql -u root -p$mysqlpasswd <<< $"FLUSH PRIVILEGES; EXIT;" &> /dev/null 2>&1;
printf "Done importing database.\n"

printf "Securing and modifying Apache configuration...\n"
#enable ssl apache2 support module
( (
printf "Enabling Apache SSL module...\n"
a2enmod ssl;
#restart the service to recognize the change
printf "Restarting Apache server...\n"
service apache2 restart;
#make directory to place certs
printf "Creating SSL Directory...\n"
mkdir -p /etc/apache2/ssl;
#create both key and certificate for the website
printf "Generating self signed certificate and RSA key...\n"
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/apache2/ssl/apache.key -out /etc/apache2/ssl/apache.crt <<< $'US\nCalifornia\nLos Angeles\n424Sec\nCyber\n192.168.1.120\n\n' &> /dev/null 2>&1;
#create a backup for conf files before we overwrite them
printf "Backing up default configuration files...\n"
cp /etc/apache2/sites-available/default-ssl.conf /etc/apache2/sites-available/default-ssl.conf.bak;
cp /etc/apache2/sites-available/000-default.conf /etc/apache2/sites-available/000-default.com.bak;
cp /etc/apache2/conf-enabled/security.conf /etc/apache2/conf-enabled/security.conf.bak
cp /etc/apache2/mods-enabled/dir.conf /etc/apache2/mods-enabled/dir.conf.bak
#next two printf change the conf files to setup ssl
#the ip variable will hold the servers current ip address to make sure it's correctly assigned
ip="$(ifconfig | grep -m 1 "inet " | awk -F'[: ]+' '{ print $4 }')"
#modifies the conf to auto redirect any http to https
printf "Setting up HTTP redirect to HTTPS...\n"
printf "
<VirtualHost *:80>
        \tServerAdmin root@424sec.com
        \tDocumentRoot /var/www/html
        \tErrorLog ${APACHE_LOG_DIR}/error.log
        \tCustomLog ${APACHE_LOG_DIR}/access.log combined
        \tServerName %s
        \tRedirect permanent / https://%s
</VirtualHost>
" $ip $ip > /etc/apache2/sites-available/000-default.conf;
#secure the server using the proper SSL configuration
#SSLCompression off > Defends against CRIME Attack
#SSLProtocol ... > Defends against SSL downgrade / POODLE
#SSLCipherSuite > cipher suites that provide Perfect Forward Secrecy
printf "Configuring SSL...\n"
printf "
<IfModule mod_ssl.c>
        <VirtualHost _default_:443>
                ServerAdmin root@424sec.com
                ServerName 424sec.com
                ServerAlias www.424sec.com
                DocumentRoot /var/www/html
                ErrorLog ${APACHE_LOG_DIR}/error.log
                CustomLog ${APACHE_LOG_DIR}/access.log combined
                SSLEngine on
                SSLCompression off
                SSLProtocol All -SSlv2 -SSLv3 -TLSv1
                SSLCipherSuite EECDH+AESGCM:EDH+AESGCM:AES256+EECDH:AES256+EDH
                SSLCertificateFile /etc/apache2/ssl/apache.crt
                SSLCertificateKeyFile /etc/apache2/ssl/apache.key
                <FilesMatch \"\.(cgi|shtml|phtml|php)$\">
                                SSLOptions +StdEnvVars
                </FilesMatch>
                <Directory /usr/lib/cgi-bin>
                                SSLOptions +StdEnvVars
                </Directory>
                BrowserMatch \"MSIE [2-6]\" \\
                \tnokeepalive ssl-unclean-shutdown \\
                \tdowngrade-1.0 force-response-1.0
            BrowserMatch \"MSIE [17-9]\" ssl-unclean-shutdown
        </VirtualHost>
</IfModule>
" > /etc/apache2/sites-available/default-ssl.conf;
#remove server information so attacker cannot gain extra info
printf "Putting server into production mode...\n"
printf "
ServerTokens Prod
ServerSignature Off
TraceEnable Off
" > /etc/apache2/conf-enabled/security.conf
#tell apache to look at .php files first before any other extension
printf "Configuring directory settings...\n"
printf "
<IfModule mod_dir.c>
        DirectoryIndex index.php index.cgi index.pl index.html index.xhtml index.htm
</IfModule>
" > /etc/apache2/mods-enabled/dir.conf
#remove the apache2 default index.html it is not needed
printf "Removing default index.html...\n"
rm /var/www/html/index.html
#enable the ssl virtual host for ssl connection
printf "Enabling SSL configuration...\n"
a2ensite default-ssl.conf;
#restart the apache server to recognize the changes
printf "Restarting Apache...\n"
service apache2 restart;
printf "Apache ready..\nHave a nice day.\n"
) &)
