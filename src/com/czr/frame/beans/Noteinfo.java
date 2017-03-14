package com.czr.frame.beans;

import java.util.Date;
import java.io.Serializable;
import java.lang.Long;

 /**  
 * @Title:  日记信息
 * @Description:日记信息
 * @author: chenzhirong  
 * @date:2017-03-14 03:53:45
 * @version V1.0
 */  
public class Noteinfo implements Serializable {


    /** OBJ_ID -  */
    private Long objId;
    /** TITLE -  */
    private String title;
    /** TAG -  */
    private String tag;
    /** LEVE -  */
    private Long leve;
    /** CREATETIME -  */
    private Date createtime;
    /** UPDATETIME -  */
    private Date updatetime;
    /** CONTENT -  */
    private String content;


    public Long getObjId(){
        return this.objId;
    }
    public void setObjId(Long objId){
        this.objId = objId;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getTag(){
        return this.tag;
    }
    public void setTag(String tag){
        this.tag = tag;
    }

    public Long getLeve(){
        return this.leve;
    }
    public void setLeve(Long leve){
        this.leve = leve;
    }

    public Date getCreatetime(){
        return this.createtime;
    }
    public void setCreatetime(Date createtime){
        this.createtime = createtime;
    }

    public Date getUpdatetime(){
        return this.updatetime;
    }
    public void setUpdatetime(Date updatetime){
        this.updatetime = updatetime;
    }

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
    }
}