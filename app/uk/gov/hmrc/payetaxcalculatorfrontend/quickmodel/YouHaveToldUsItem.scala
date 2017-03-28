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

package uk.gov.hmrc.payetaxcalculatorfrontend.quickmodel

import play.api.i18n.Messages
import uk.gov.hmrc.payetaxcalculatorfrontend.controllers.routes

case class YouHaveToldUsItem(value: String, label: String, url: String, idSuffix: String)

trait YouHaveToldUs[A] {
  def toYouHaveToldUsItem(t: A): YouHaveToldUsItem
}

object YouHaveToldUs {
  def apply[A : YouHaveToldUs](a: A): YouHaveToldUsItem = implicitly[YouHaveToldUs[A]].toYouHaveToldUsItem(a)

  implicit def taxCodeFormat(implicit messages: Messages): YouHaveToldUs[UserTaxCode] = new YouHaveToldUs[UserTaxCode] {
    def toYouHaveToldUsItem(t: UserTaxCode): YouHaveToldUsItem = {
      val label = "about_tax_code"
      val idSuffix = "tax-code"
      val url = routes.QuickCalcController.showTaxCodeForm().url
      YouHaveToldUsItem(
        if(t.gaveUsTaxCode) t.taxCode.getOrElse(UserTaxCode.DEFAULT_TAX_CODE)
        else s"${Messages("quick_calc.you_have_told_us.about_tax_code.no")}" + " (" +
          s"${Messages("quick_calc.you_have_told_us.about_tax_code.default")} " +
          s"${t.taxCode.getOrElse(UserTaxCode.DEFAULT_TAX_CODE)}" + ")",
        label, url, idSuffix)
    }
  }

  implicit def overStatePensionAgeFormat(implicit messages: Messages) = new YouHaveToldUs[OverStatePensionAge] {
    def toYouHaveToldUsItem(overStatePensionAge: OverStatePensionAge): YouHaveToldUsItem = {
      val label = "over_state_pension_age"
      val idSuffix = "pension-state"
      val url = routes.QuickCalcController.showStatePensionForm().url
      YouHaveToldUsItem(
        if(overStatePensionAge.value) Messages("quick_calc.you_have_told_us.over_state_pension_age.yes")
        else Messages("quick_calc.you_have_told_us.over_state_pension_age.no"), label, url, idSuffix)
    }
  }

  implicit def scottishIncomeFormat(implicit messages: Messages) = new YouHaveToldUs[ScottishRate] {
    def toYouHaveToldUsItem(scottish: ScottishRate): YouHaveToldUsItem = {
      val label = "scottish_rate"
      val idSuffix = "scottish_rate"
      val url = routes.QuickCalcController.showScottishRateForm().url
      YouHaveToldUsItem(
        if(scottish.value) Messages("quick_calc.you_have_told_us.scottish_rate.yes")
        else Messages("quick_calc.you_have_told_us.scottish_rate.no"), label, url, idSuffix)
    }
  }

  implicit def salaryFormat(implicit messages: Messages) = new YouHaveToldUs[Salary] {
    def toYouHaveToldUsItem(s: Salary): YouHaveToldUsItem = {
      val url = routes.QuickCalcController.showSalaryForm().url
      val idSuffix = "income"
      def asPounds(v: BigDecimal) = "£" + v

      YouHaveToldUsItem(asPounds(s.amount), s.period, url, idSuffix)
    }
  }

  implicit def salaryPeriodFormat(implicit messages: Messages) = new YouHaveToldUs[Detail] {
    def toYouHaveToldUsItem(detail: Detail): YouHaveToldUsItem = {
      val label = s"${detail.period}_sub"
      val idSuffix = "salary_period"
      val url = routes.QuickCalcController.showSalaryForm().url
      YouHaveToldUsItem(
        detail.howManyAWeek.toString, label, url, idSuffix)
    }
  }

  def formatForIndividualSalary[T <: Salary](implicit m: Messages): YouHaveToldUs[T] = new YouHaveToldUs[T] {
    def toYouHaveToldUsItem(salary: T) = salaryFormat.toYouHaveToldUsItem(salary)
  }
}