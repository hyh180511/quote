package com.demo.quote.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectInfo implements Serializable {
    private int id; //项目自增id ，前端直接保存
    private String name; //项目名
    private String address; //建筑地址
    private int num; //楼层栋数
    private int accountId;   //账号id，放在session里，前端取不到可以不用处理
}
