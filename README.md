# Mini-Parsers - An API for parsing discrete types

[![Maven Central](https://img.shields.io/maven-central/v/io.heretical/mini-parsers-core)](https://search.maven.org/search?q=g:io.heretical+mini-parsers)
[![javadoc](https://javadoc.io/badge2/io.heretical/mini-parsers-core/javadoc.svg?label=javadoc+mini-parsers-core)](https://javadoc.io/doc/io.heretical/mini-parsers-core)
[![javadoc](https://javadoc.io/badge2/io.heretical/mini-parsers-temporal/javadoc.svg?label=javadoc+mini-parsers-temporal)](https://javadoc.io/doc/io.heretical/mini-parsers-temporal)

## Overview

Mini-Parsers is a Java API for parsing short discrete text strings into native types where a single type may have
multiple textual representations.

This is useful for supporting command line argument values piped or pasted from different sources, data entry
validation, or normalizing data in a column during data cleansing and ETL.

For example, the same instant in time (`java.time.Instant`) may have multiple formats. The two strings `1423526400000`
and `2015-02-10T02:04:30+00:00` are equivalent if the first is interpreted as the milliseconds since the epoch.

Absolute, duration, and relative adjuster text representation disambiguation is currently supported. 

But with plans for handling more complex relative temporal representations like `10 days ago` or `last tuesday`. Also
support for common units of measure are being considered.

Final Releases are available on Maven Central:

```gradle
implementation 'io.heretical:mini-parsers-temporal:2.0.0'
```

```xml
<dependency>
  <groupId>io.heretical</groupId>
  <artifactId>mini-parsers-temporal</artifactId>
  <version>2.0.0</version>
  <type>pom</type>
</dependency>
```

WIP release maven coordinates can be found at:

* https://github.com/orgs/Heretical/packages?repo_name=mini-parsers

This library requires Java 8 and the parsing functionality is dependent on [Parboiled](https://github.com/sirthias/parboiled/wiki).

## Usage

For the most comprehensive examples on usage, see the unit tests for each subproject.

### Temporal Parsers Syntax

#### Durations

The class `DurationParser` can distinguish between either ISO-8601 duration strings, like `PT20.345S`, or simplified
natural language duration strings, like `10 days` or `15min`, and resolve them into `java.time.Duration` instances.

```java
// optionally set the Clock, ZoneId, and Locale
Context context = new Context();

DurationParser durationParser = new DurationParser( context );

java.time.Duration natural = durationParser.parseOrFail( "1 min" ).getResult();

java.time.Duration iso = durationParser.parseOrFail( "PT1M" ).getResult();
```

##### ISO-8601 Duration

To parse only ISO-8601 durations, use the parser `ISODurationParser`.

Note this parser currently calls `Duration.parse()` under the hood and does not currently offer any additional
functionality. Detecting and handling dual `java.time.Period` and `java.time.Duration` is eventually possible.

##### Natural

To parse a simplified natural language string, use the parser `NaturalDurationParser`.

A natural duration string is of the format:

```text
number[:space:]unit
```

Where `number` is any natural integer (with commas).

And `unit` is either the Unit name or Abbreviation, case-insensitive:

| Unit         | Abbreviation | Example   |
|--------------|--------------|-----------|
| Milliseconds | ms           | 300ms     |
| Seconds      | s, sec       | 30s 30sec |
| Minutes      | m, min       | 20m 20min |
| Hours        | h, hrs       | 3h 3hrs   |
| Days         | d, days      | 5d 5 days |
| Weeks        | w, wks       | 2w 2wks   |
| Months       | mos          | 3mos      |
| Years        | y, yrs       | 2y 2rs    |

For example, `10000ms` and `10,000 milliseconds` are equivalent.

#### Date/Times

Absolute and relative date/time strings are supported.

##### Absolute

The class `AbsoluteDateTimeParser` can distinguish between many date, time, and date/time strings and resolve them into
`java.time.Instant` instances.

```java
// optionally set the Clock, ZoneId, and Locale
Context context = new Context();

AbsoluteDateTimeParser parser = new AbsoluteDateTimeParser( context );

java.time.Instant iso = parser.parseOrFail( "2015-02-10T02:04:30+00:00" ).getResult();

java.time.Instant alt = parser.parseOrFail( "2015-02-10 02:04:30" ).getResult();

java.time.Instant full = parser.parseOrFail( "february 10th 2015, 02:04:03" ).getResult();

java.time.Instant amPM = parser.parseOrFail( "02/10/15 02:04 AM" ).getResult();

java.time.Instant us = parser.parseOrFail( "Feb/10/15 02:04" ).getResult();

java.time.Instant epoch = parser.parseOrFail( "1423526400000" ).getResult();
```

See [DateTimeFormats](mini-parsers-temporal/src/main/java/heretical/parser/temporal/format/DateTimeFormats.java) for a
comprehensive list of formats supported.

This parser builds a grammar for all specified formats. When applied and a match is made, an appropriate instance of
`java.time.format.DateTimeFormatter` for that date/time format is used to parse the value. The parsed result is then
coaxed into a `Instant` after applying any context values like `Locale` or `ZoneId`.

Note the grammar is used to search for the actual formatter to use instead of attempting every `DateFormatterInstance`.

##### Relative

The class `RelativeDateTimeAdjusterParser` will adjust the current time based on a simple adjustment syntax, and return an
`Instant`.

The syntax is adopted from [Splunk](https://docs.splunk.com/Documentation/Splunk/latest/Search/Specifytimemodifiersinyoursearch).

```java
// optionally set the Clock, ZoneId, and Locale
Context context = new Context();

RelativeDateTimeAdjusterParser parser = new RelativeDateTimeAdjusterParser( context );

java.time.Instant hourAgo = parser.parseOrFail( "-60m" ).getResult();

java.time.Instant hourAgoOnTheHour = parser.parseOrFail( "-1h@h" ).getResult();

java.time.Instant weekAgoToday = parser.parseOrFail( "-7d@d" ).getResult();

java.time.Instant beginningOfCurrentWeek = parser.parseOrFail( "@w0" ).getResult(); // depends on locale

java.time.Instant tomorrow = parser.parseOrFail( "+24h@s" ).getResult();
```




