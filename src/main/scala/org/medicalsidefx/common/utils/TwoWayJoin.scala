package org.medicalsidefx.common.utils

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
 * Created by sansub01 on 11/19/14.
 */
object TwoWayJoin {
  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage: TwoWayJoinCount <file1>   <file2>")
      System.exit(12)
    }

    val sconf = new SparkConf().setMaster("local").setAppName("MedicalSideFx-TwoWayJoin")

    val sc = new SparkContext(sconf)

    val file1 = args(0)
    val file2 = args(1)

    val file1Rdd = sc.textFile(file1).map(x => (x.split(",")(0), x.split(",")(1)))
    val file2Rdd = sc.textFile(file2).map(x => (x.split(",")(0), x.split(",")(1)))

    val f1Joinf2 = file1Rdd.join(file2Rdd)
    f1Joinf2.foreach(println)
    f1Joinf2.map(x => (x.toString().replace("(","").replace(")","").split(",")(0) + "=" +
                       x.toString().replace("(","").replace(")","").split(",")(1),
                       x.toString().replace("(","").replace(")","").split(",")(2))).reduceByKey((v1,v2) => (v1+";"+v2)).foreach(println)
  }
}
