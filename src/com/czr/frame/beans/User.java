package com.czr.frame.beans;

import java.util.Date;
import java.io.Serializable;
import java.lang.Long;

 /**  
 * @Title:  用户信息表
 * @Description:用户信息表
 * @author: chenzhirong  
 * @date:2017-03-14 03:54:03
 * @version V1.0
 */  
public class User implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4011002578122586992L;

	/** OBJ_ID - 主键 */
    private Long objId;

    /** USERNAME - 用户姓名 */
    private String username;
    /** USERCODE - 用户代码 */
    private String usercode;
    /** PASSWORD - 用户密码 */
    private String password;
    /** EMAIL - 邮箱 */
    private String email;
    /** JB - 级别 */
    private Long jb;
    /** CREATETIME - 创建时间 */
    private Date createtime;
    /** UPDATETIME - 更新时间 */
    private Date updatetime;
    /** ZT - 状态 1=正常 0=删除 */
    private Long zt;


    public Long getObjId(){
        return this.objId;
    }
    public void setObjId(Long objId){
        this.objId = objId;
    }

    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getUsercode(){
        return this.usercode;
    }
    public void setUsercode(String usercode){
        this.usercode = usercode;
    }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public Long getJb(){
        return this.jb;
    }
    public void setJb(Long jb){
        this.jb = jb;
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

    public Long getZt(){
        return this.zt;
    }
    public void setZt(Long zt){
        this.zt = zt;
    }
}