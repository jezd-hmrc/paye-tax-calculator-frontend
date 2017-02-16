/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.payetaxcalculatorfrontend.model

import play.api.data.FormError
import play.api.data.format.Formatter
import play.api.i18n.Messages

object CustomFormatters {

  def requiredBooleanFormatter: Formatter[Boolean] = new Formatter[Boolean] {
    override def bind(key: String, data: Map[String, String]) = {
      Right(data.getOrElse(key,"")).right.flatMap {
        case "true" => Right(true)
        case "false" => Right(false)
        case _ => Left(Seq(FormError(key,"Please select one of these options.")))
      }
    }
    override def unbind(key: String, value: Boolean): Map[String, String] = Map(key -> value.toString)
  }

  def dayValidation(implicit messages: Messages): Formatter[Int] = new Formatter[Int] {
    override def bind(key: String, data: Map[String, String]) = {
      Right(data.getOrElse(key,"")).right.flatMap {
        case s if s.nonEmpty =>
          try {
            val days = s.toInt
            if(days < 0) {
              Left(Seq(FormError(key, Messages("quick_calc.salary.question.error.number_of_days.less_than_zero"))))
            } else if(days > 7) {
              Left(Seq(FormError(key, Messages("quick_calc.salary.question.error.number_of_days.more_than_seven"))))
            } else {
              Right(days)
            }
          }
          catch {
            case _: Throwable => Left(Seq(FormError(key, Messages("quick_calc.salary.question.error.need_number"))))
          }
        case _ => Left(Seq(FormError(key, Messages("quick_calc.salary.question.error.empty_number"))))
      }
    }

    override def unbind(key: String, value: Int): Map[String, String] = Map(key -> value.toString)
  }

  def hoursValidation(implicit messages: Messages): Formatter[Int] = new Formatter[Int] {
    override def bind(key: String, data: Map[String, String]) = {
      Right(data.getOrElse(key,"")).right.flatMap {
        case s if s.nonEmpty  =>
          try {
            val hours = s.toInt
            if(hours < 1) {
              Left(Seq(FormError(key, Messages("quick_calc.salary.question.error.number_of_hours.less_than_one"))))
            } else if(hours > 168) {
              Left(Seq(FormError(key, Messages("quick_calc.salary.question.error.number_of_hours.more_than_168"))))
            } else {
              Right(hours)
            }
          }
          catch {
            case _: Throwable => Left(Seq(FormError(key, Messages("quick_calc.salary.question.error.need_number"))))
          }
        case _ => Left(Seq(FormError(key, Messages("quick_calc.salary.question.error.empty_number"))))
      }
    }

    override def unbind(key: String, value: Int): Map[String, String] = Map(key -> value.toString)

  }

  def salaryValidation(salaryType:String): Formatter[BigDecimal] = new Formatter[BigDecimal] {
    override def bind(key: String, data: Map[String, String]) = {
      Right(data.getOrElse(key,"")).right.flatMap {
        case s =>
          val salary = BigDecimal(s.toInt)
          if(salary < 0.01) {
            Left(Seq(FormError(key, "The gross pay must be more than zero")))
          } else if(salary > 9999999.99) {
            Left(Seq(FormError(key, "Maximum value for gross pay is £9,999,999.99")))
          } else {
            Right(salary)
          }
        case _ => Left(Seq(FormError(key, "Please enter your " + s"$salaryType" + " gross pay")))
      }
    }

    override def unbind(key: String, value: BigDecimal): Map[String, String] = ???

  }
}
