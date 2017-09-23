#!/usr/bin/env bash
rsync -tv /home/travis/build/MrKajmaK/MCTrapsShop/target/MCTrapsShop.jar $FTP_USER@$FTP_SERVER:$FTP_PATH