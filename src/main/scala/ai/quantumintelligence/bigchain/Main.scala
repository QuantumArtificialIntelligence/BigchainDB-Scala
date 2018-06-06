package ai.quantumintelligence.bigchain

import com.authenteq.api.AssetsApi
import com.authenteq.builders._
import net.i2p.crypto.eddsa._

import collection.JavaConverters._

object DataStructures {
  trait BlockchainData
  case class MyGreatestData(id: String, information: String) extends BlockchainData
}

object Main extends App {
  import DataStructures._

  // We create a new pair of public and private keys
  val edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator()
  val keyPair = edDsaKpg.generateKeyPair()

  // This is the data that is going to be stored in the blockchain
  val gd = MyGreatestData("id-1","This is the information that goes with the new block!")

  // Since we have the docker containers running we connect to our local version of BigchainDB
  BigchainDbConfigBuilder.baseUrl("http://localhost:9984").addToken("app_id", "MyToken").addToken("app_key", "MyAppKey").setup()

  // We are here it means we are connected to BigChainDB, however additional validations must be added to the previous line
  // to validate that the connection was successful.
  // The next 4 lines create a new Transaction with our information that is stored in variable `gd`, then sign and send it
  val transaction = BigchainDbTransactionBuilder.init()
    .addAssets(gd, gd.getClass)
    .buildAndSign(keyPair.getPublic.asInstanceOf[EdDSAPublicKey], keyPair.getPrivate.asInstanceOf[EdDSAPrivateKey])
    .sendTransaction()

  // We wait a few secs while BigchainDB does its magic, creating and commiting the block to the chain and the assets
  println(s"Waiting 10 secs for the transaction to be processed by BigchainDB")
  Thread.sleep(10000)

  // Then we want to make sure that the transaction and our information was successful inserted in the DB

  val assetsResult = AssetsApi.getAssets(s""""id-1"""")

  println(s"There are ${assetsResult.size()} found with id-1, here they are: ${assetsResult.getAssets.asScala.map(_.getData.toString).mkString("\n")}")


}

