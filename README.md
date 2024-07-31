# To-Do Today API.

To-Do Today RestApi to be used with <a href="https://github.com/S4nchzz/To-Do-Today_dapp"> To-Do Today Desktop app</a>

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

The things you need before installing the software.

* <a href="https://www.oracle.com/java/technologies/javase/jdk22-archive-downloads.html">Java 22 </a>
* <a href="https://mariadb.org/">Maria DB</a>
* <a href="https://www.heidisql.com/">Heidi SQL (Optional)</a>
* <a href="https://www.apachefriends.org/es/index.html">XAMPP</a>

### Installation

A step by step guide that will tell you how to get the development environment up and running.

* Clone or download the repo
* Change the launch.json parameters on the /.vscode folder
* On HeidiSQL or MariaDB Server ensure to copy and paste the entire database from ../to_do_today_api/repo/dbLocalSetup.md
* Change the spring.datasource.url on ../src/main/resources/application.properties to match with your port, you should leave localhost if you are using the same machine to store the database
* Compile
