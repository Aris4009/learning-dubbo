package com.example.db1.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName(value = "user")
public class User implements Serializable {

	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	private String name;
}
