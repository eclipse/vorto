#!/bin/bash
#
# Copyright (c) 2020 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License 2.0 which is available at
# https://www.eclipse.org/legal/epl-2.0
#
# SPDX-License-Identifier: EPL-2.0
#


# Script to monitor Vorto website and generators.
#
# urls.lst - all urls to monitor
# GEN_URL - URLS to get all registered generators
# EMAIL -  Email to sent for alerts

URLS=urls.lst
GEN_URL=http://vorto.eclipse.org/repo/rest/generation-router/platform
EMAIL=vorto-dev@eclipse.org


function checkurl {
  response=$(curl --write-out %{http_code} --silent --output /dev/null $1)

  if [ $response == "200" ] || [ $response == "401" ]; then    
    echo `date` $1 " working!!!"
  else
	echo $1 " not working!!!. http_code: $response. Sending email to -> " $EMAIL;	
	echo "$1 Website is down" `date` | mail -s "$1 Website is down. Please check." "$EMAIL"
  fi
}

function check-all-generators {
  urls=$(curl -s -X GET $GEN_URL | ./JSON.sh | egrep '\[*,"generatorInfoUrl"\]' | awk -F " " '{ print $2 }' | tr -d '"')
  if [ -n "$output" ]; then
    while read -r url; do
      echo "Checking $url ..."
      checkurl $url
    done <<< "$urls"
  fi
}

# Check for all urls
while read url; do
  checkurl $url
done < $URLS

# Check for all generators
check-all-generators
