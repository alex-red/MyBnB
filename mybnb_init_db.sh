#!/usr/bin/env bash
export MYSQL_PWD='root'
echo "Loading Schema into MySQL server"
mysql -u root < ./mybnb_schema.sql
echo "Loading Data into MySQL server"
mysql -u root < ./mybnb_data.sql
