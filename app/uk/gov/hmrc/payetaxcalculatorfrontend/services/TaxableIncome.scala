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

package uk.gov.hmrc.payetaxcalculatorfrontend.services

import uk.gov.hmrc.payetaxcalculatorfrontend.utils.BigDecimalUtils.max

object TaxableIncome {
  def calculate(taperedAllowanceLimit: Int, defaultPersonalAllowance: Int)(earnings: BigDecimal): BigDecimal = {
    val taperedAllowanceDeduction = max((earnings - taperedAllowanceLimit) / 2, 0)
    val adjustedPersonalAllowance = max(defaultPersonalAllowance - taperedAllowanceDeduction, 0)
    max(earnings - adjustedPersonalAllowance, 0)
  }
}

