package com.czr.frame.beans;

import java.io.Serializable;
import java.lang.Long;

 /**  
 * @Title:  日记标签
 * @Description:日记标签
 * @author: chenzhirong  
 * @date:2017-03-14 03:53:57
 * @version V1.0
 */  
public class Tag implements Serializable {

    /** OBJ_ID - ID主键 */
    private Long objId;

    /** TAGNAME - 标签名称 */
    private String tagname;
    /** TAGCODE - 标签代码 */
    private String tagcode;


    public Long getObjId(){
        return this.objId;
    }
    public void setObjId(Long objId){
        this.objId = objId;
    }

    public String getTagname(){
        return this.tagname;
    }
    public void setTagname(String tagname){
        this.tagname = tagname;
    }

    public String getTagcode(){
        return this.tagcode;
    }
    public void setTagcode(String tagcode){
        this.tagcode = tagcode;
    }
}