#!/bin/sh

export DBMAINTAIN_JDBC_DRIVER=dbm/ojdbc6-11.2.0.3.jar
export DBMAINTAIN_OPTS="-Duser.country=TR -Duser.language=en"

dbm/dbmaintain.sh $1 dbscripts -config hopedb.properties