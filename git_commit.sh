#!/bin/bash -e
if test $# -eq 0
then
  echo "commit.sh: Meteme el mensaje para el comit entre comillas dobles \"\" "
  exit

else
  if test $# -gt 1
  then
    echo "demasiados argumentos"
    exit
  fi
fi

#echo $1
git add -A
git commit -m "$1"
git push -u origin main
echo "usuario github:	vendul0g"
echo "token github: 	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"
