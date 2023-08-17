package com.xiaoguan.front.service;

import com.xiaoguan.front.UpdateException;

public interface RealNameService {
    boolean judgeNameAndCard(String name,String idCard,String phone) throws UpdateException;
}
