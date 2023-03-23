Citrus DB - JDBC server simulation ![Logo][1]
==============

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.consol.citrus/citrus-db/badge.svg?style=flat-square)](https://central.sonatype.com/search?q=g%253Acom.consol.citrus)
[![build](https://github.com/citrusframework/citrus-db/workflows/build/badge.svg?branch=main)](https://github.com/citrusframework/citrus-db/actions)
[![Javadocs](http://javadoc.io/badge/com.consol.citrus/citrus-db-server.svg)](http://javadoc.io/doc/com.consol.citrus/citrus-db-server)
[![Licensed under Apache License version 2.0](https://img.shields.io/github/license/openshift/origin.svg?maxAge=2592000)](https://www.apache.org/licenses/LICENSE-2.0")
[![Chat on Zulip](https://img.shields.io/badge/zulip-join_chat-brightgreen.svg)](https://citrusframework.zulipchat.com)

This library is used to simulate JDBC client-server communication. Usually the system under test (SUT) uses a JDBC driver in order to connect to a relational database such as MySQL, Oracle, DB2, or something else. The Citrus JDBC server is able to simulate the database server.

The server accepts connections and SQL queries such as CREATE TABLE, SELECT, DELETE, UPDATE, INSERT and so on in order to respond with simulated JDBC result sets. The user is able to validate the incoming connections as well as the requested SQL syntax. The result sets are defined as predefined set of Json data sets.

With this approach testing the database persistence of an application in an integration or end-to-end test scenario becomes way more easier than setting up a test database instance with all tables and test data.

Getting started
---------

The repository provides both database driver and server for relational DBMS simulation via JDBC.

Licensing
---------
  
Copyright 2018 ConSol Software GmbH.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
  
Consulting
---------

Just in case you need professional support for Citrus have a look at
[https://citrusframework.org/contact.html](https://citrusframework.org/contact.html).
Contact `citrus-dev@googlegroups.com` directly for any request

Bugs
---------

Please report any bugs and/or feature requests to `citrus-dev@googlegroups.com`
or directly to [http://github.com/citrusframework/citrus-db/issues](http://github.com/citrusframework/citrus-db/issues)
  
Information
---------

For more information on Citrus see [www.citrusframework.org][2], including
a complete [reference manual][3].

 [1]: https://citrusframework.org/img/brand-logo.png "Citrus"
 [2]: https://citrusframework.org
 [3]: https://citrusframework.org/reference/html/
