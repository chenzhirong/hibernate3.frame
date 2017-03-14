package com.czr.frame.beans;

import java.io.Serializable;
import java.lang.Long;

 /**  
 * @Title:  日记附件
 * @Description:日记附件
 * @author: chenzhirong  
 * @date:2017-03-14 03:53:39
 * @version V1.0
 */  
public class Attachment implements Serializable {

    /** OBJ_ID - 主键ID */
    private Long objId;

    /** SUFFIX - 后缀 */
    private String suffix;
    /** FJNR - 附件内容 */
    private byte[] fjnr;
    /** ZT - 状态 1=正常，0=删除 */
    private Long zt;


    public Long getObjId(){
        return this.objId;
    }
    public void setObjId(Long objId){
        this.objId = objId;
    }

    public String getSuffix(){
        return this.suffix;
    }
    public void setSuffix(String suffix){
        this.suffix = suffix;
    }

    public byte[] getFjnr(){
        return this.fjnr;
    }
    public void setFjnr(byte[] fjnr){
        this.fjnr = fjnr;
    }

    public Long getZt(){
        return this.zt;
    }
    public void setZt(Long zt){
        this.zt = zt;
    }
}