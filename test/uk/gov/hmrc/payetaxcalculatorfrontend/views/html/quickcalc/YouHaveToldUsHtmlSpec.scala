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

package uk.gov.hmrc.payetaxcalculatorfrontend.views.html.quickcalc

import org.jsoup.Jsoup
import org.scalatestplus.play.OneAppPerSuite
import play.api.i18n.Messages.Implicits._
import play.api.test.FakeRequest
import uk.gov.hmrc.payetaxcalculatorfrontend.model.YouHaveToldUsItem
import uk.gov.hmrc.play.test.UnitSpec

class YouHaveToldUsHtmlSpec extends UnitSpec with OneAppPerSuite {

  implicit val request = FakeRequest()

  "Html snippet for the `You Have Told Us` section" should {
    "not appear if there are no items" in {
      val html = you_have_told_us(Nil)

      val parsedHtml = Jsoup.parse(html.body)

      parsedHtml.getElementsByTag("td").size shouldBe 0
    }
    "show a row for each item + a table header" in {
      val items = List(
        YouHaveToldUsItem("1150L", "Tax Code", "/foo"),
        YouHaveToldUsItem("Yes", "Over 65", "/bar")
      )

      val html = you_have_told_us(items)

      val parsedHtml = Jsoup.parse(html.body)
      val expectedNumberOfRows = 1 + items.size // header + 1 row for each item
      parsedHtml.getElementsByTag("tr").size shouldBe expectedNumberOfRows
    }
  }

}