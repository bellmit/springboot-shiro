package com.sq.transportmanage.gateway.dao.entity.mdbcarmanage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarAdmUser {
  private Date updateDate;

  private String cities;

  private String suppliers;

  private Integer level;

  private Integer roleId;

  private Integer accountType;

  private String updateUser;

  private String remark;

  private String userName;

  private Integer userId;

  private String password;

  private String phone;

  private String teamId;

  private String groupIds;

  private String createUser;

  private String account;

  private String email;

  private Date createDate;

  private Integer status;
}
