/*
 * Copyright 2019 WeBank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.wedatasphere.linkis.bml.request

import java.io.{File, InputStream}
import java.util

import com.webank.wedatasphere.linkis.bml.http.HttpConf
import com.webank.wedatasphere.linkis.httpclient.request._

/**
  * created by cooperyang on 2019/5/23
  * Description:
  */

abstract class BmlPOSTAction extends POSTAction

abstract class BmlGETAction extends GetAction


/**
  *  BmlUpload
  * @param filePaths
  * @param _inputStreams
  */
case class BmlUploadAction(filePaths:Array[String],
                           _inputStreams:util.Map[String,InputStream]) extends BmlPOSTAction with UploadAction{

  private val streamNames = new util.HashMap[String,String]

  override val files: util.Map[String, String] = {
    if (null == filePaths || filePaths.length == 0) new util.HashMap[String,String]() else{
      val map = new java.util.HashMap[String, String]
      filePaths foreach {
        filePath => val arr = filePath.split(File.separator)
          val fileName = arr(arr.length - 1)
          map.put("file", filePath)
      }
      map
    }
  }

  override def inputStreams: util.Map[String, InputStream] = _inputStreams

  override def inputStreamNames: util.Map[String, String] = streamNames

  //  override def inputStreams: util.Map[String, InputStream] = {
  //    if (files.size() == 0) new util.HashMap[String, InputStream]() else{
  //      val map = new util.HashMap[String, InputStream]()
  //      files foreach {
  //        case (fileName, filePath) => val fs = FSFactory.getFs(new FsPath(filePath))
  //          fs.init(null)
  //          val inputStream = fs.read(new FsPath(filePath))
  //
  //      }
  //    }
  //  }

  private var _user:String = _

  override def setUser(user: String): Unit = this._user = user

  override def getUser: String = this._user

  override def getRequestPayload: String = ""

  override def getURL: String = HttpConf.uploadURL
}

case class BmlUpdateAction(filePaths:Array[String],
                           _inputStreams:util.Map[String,InputStream]) extends BmlPOSTAction with UploadAction{
  override def getURL: String = HttpConf.updateVersionURL

  override def getRequestPayload: String = ""

  private var _user:String = _

  private val streamNames = new util.HashMap[String,String]

  override val files: util.Map[String, String] = {
    if (null == filePaths || filePaths.length == 0) new util.HashMap[String,String]() else{
      val map = new java.util.HashMap[String, String]
      filePaths foreach {
        filePath => val arr = filePath.split(File.separator)
          val fileName = arr(arr.length - 1)
          map.put("file", filePath)
      }
      map
    }
  }

  override def setUser(user: String): Unit = this._user = user

  override def getUser: String = this._user
  override def inputStreams: util.Map[String, InputStream] = _inputStreams

  override def inputStreamNames: util.Map[String, String] = streamNames
}


case class BmlDownloadAction() extends BmlGETAction with DownloadAction with UserAction{

  private var inputStream:InputStream = _
  private var user:String = _

  def getInputStream:InputStream = this.inputStream

  def setInputStream(inputStream: InputStream):Unit = this.inputStream = inputStream

  override def getURL: String = HttpConf.downloadURL

  override def write(inputStream: InputStream): Unit = this.inputStream = inputStream

  override def setUser(user: String): Unit = this.user = user

  override def getUser: String = this.user
}




case class BmlRelateAction(user:String,
                           resourceId:String,
                           inputStream: InputStream) extends BmlPOSTAction{
  override def getRequestPayload: String = ""

  override def getURL: String = HttpConf.updateVersionURL
}


case class BmlGetVersionsAction(user:String,
                                resourceId:String) extends BmlPOSTAction{
  override def getRequestPayload: String = ""

  override def getURL: String = HttpConf.getVersionsUrl
}


case class BmlUpdateBasicAction(properties:java.util.Map[String, String]) extends BmlPOSTAction{
  override def getRequestPayload: String = ""

  override def getURL: String = HttpConf.updateBasicUrl
}


case class BmlGetBasicAction(resourceId:String) extends BmlGETAction with UserAction {

  private var user:String = _

  override def getURL: String = HttpConf.getBasicUrl

  override def setUser(user: String): Unit = this.user = user

  override def getUser: String = this.user
}



case class BmlDeleteAction(resourceId:String) extends BmlPOSTAction {
  override def getRequestPayload: String = ""

  override def getURL: String = HttpConf.deleteURL
}



