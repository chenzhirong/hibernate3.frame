package com.czr.frame.beans;

import java.util.Date;
import java.io.Serializable;
import java.lang.Long;

 /**  
 * @Title:  日记信息
 * @Description:日记信息
 * @author: chenzhirong  
 * @date:2017-03-14 03:53:51
 * @version V1.0
 */  
public class NoteBook implements Serializable {

    /** OBJ_ID - 主键ID */
    private Long objId;

    /** T_T_OBJ_ID - ID主键 */
    private Long tTObjId;
    /** TITLE - 标题 */
    private String title;
    /** CONTENT - 内容 */
    private String content;
    /** CREATETIME - 创建时间 */
    private Date createtime;
    /** UPDATETIME - 更新时间 */
    private Date updatetime;
    /** JM - 1=加密 0=不加密 */
    private Long jm;
    /** ATTOBJID - 附件objid */
    private Long attobjid;
    /** ZT - 状态 1=正常 0=删除 */
    private Long zt;


    public Long getObjId(){
        return this.objId;
    }
    public void setObjId(Long objId){
        this.objId = objId;
    }

    public Long gettTObjId(){
        return this.tTObjId;
    }
    public void settTObjId(Long tTObjId){
        this.tTObjId = tTObjId;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
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

    public Long getJm(){
        return this.jm;
    }
    public void setJm(Long jm){
        this.jm = jm;
    }

    public Long getAttobjid(){
        return this.attobjid;
    }
    public void setAttobjid(Long attobjid){
        this.attobjid = attobjid;
    }

    public Long getZt(){
        return this.zt;
    }
    public void setZt(Long zt){
        this.zt = zt;
    }
}