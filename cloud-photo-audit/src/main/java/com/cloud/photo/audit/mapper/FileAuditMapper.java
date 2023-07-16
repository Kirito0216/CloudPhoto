package com.cloud.photo.audit.mapper;

import com.cloud.photo.audit.entity.FileAudit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文件审核列表 Mapper 接口
 * </p>
 *
 * @author OUGE
 * @since 2023-07-14
 */
@Mapper
public interface FileAuditMapper extends BaseMapper<FileAudit> {

}
