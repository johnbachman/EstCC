package infcalcs

import org.scalatest._

class ContTableTest extends FlatSpec with Matchers {
  // Vector and table that we'll use in a lot of the following tests
  val r1 = Vector(1, 1)
  val table1 = Vector(r1, r1, r1, r1)

  "A contingency table" should "be empty if created from an empty vector" in {
    val emptyTable = Vector()
    val et = new ConstructedTable(emptyTable)
    et.rows shouldBe 0
    et.cols shouldBe 0
  }

  it should "be able to be created from a 2-D matrix of integers" in {
    val ct1 = new ConstructedTable(table1)
    ct1.rows shouldBe 4
    ct1.cols shouldBe 2
    ct1.numSamples shouldBe 8
  }

  it should "calculate probabilities from sample counts" in {
    val ct1 = new ConstructedTable(table1)
    ct1.probVect(r1) shouldBe 0.25
    ct1.table map ct1.probVect shouldBe Vector(0.25, 0.25, 0.25, 0.25)
    ct1.ttable map ct1.probVect shouldBe Vector(0.5, 0.5)
  }

  it should "calculate entropies from probabilities" in {
    val ct1 = new ConstructedTable(table1)
    val prob = ct1.probVect(r1)
    prob shouldBe 0.25
    val entropy = ct1.eTerm(prob)
    entropy shouldBe -0.5
  }

  it should "calculate marginal entropies for a 2-D table" in {
    val ct1 = new ConstructedTable(table1)
    ct1.margRowEntropy shouldBe 2
    ct1.margColEntropy shouldBe 1
  }

  it should "calculate conditional entropies for a 2-D table" in {
    val ct1 = new ConstructedTable(table1)
    ct1.condRowEntropy shouldBe 2
    ct1.condColEntropy shouldBe 1
  }

  it should "calculate mutual information" in {
    val ct1 = new ConstructedTable(table1)
    ct1.mutualInformation shouldBe 0
    val ct2 = new ConstructedTable(table1.transpose)
    ct2.mutualInformation shouldBe 0
    // Let's try another table
    val table2 = Vector(Vector(1, 0, 0, 0),
                        Vector(0, 1, 0, 0),
                        Vector(0, 0, 1, 0),
                        Vector(0, 0, 0, 1))
    val ct3 = new ConstructedTable(table2)
    ct3.mutualInformation shouldBe 2
    val ct4 = new ConstructedTable(table2.transpose)
    ct4.mutualInformation shouldBe 2
    // And another one
    val table3 = Vector(Vector(1, 1, 0, 0),
                        Vector(1, 1, 0, 0),
                        Vector(0, 0, 1, 1),
                        Vector(0, 0, 1, 1))
    val ct5 = new ConstructedTable(table3)
    ct5.mutualInformation shouldBe 1
    val ct6 = new ConstructedTable(table3.transpose)
    ct6.mutualInformation shouldBe 1
  }

  it should
    "be equal to another contingency table containing the same values" in {
    val ct1 = new ConstructedTable(Vector(Vector(1, 0), Vector(0, 1)))
    val ct2 = new ConstructedTable(Vector(Vector(1, 0), Vector(0, 1)))
    ct1.equals(ct2) shouldBe true
  }
}
