package com.reactive.pizza.repositories.persistences

import org.joda.time.DateTime
import java.sql.Timestamp

package object tables {
  implicit def toDateTime(timestamp: Timestamp): DateTime = new DateTime(timestamp)
  implicit def toTimestamp(dateTime: DateTime): Timestamp = new Timestamp(dateTime.getMillis)
}
