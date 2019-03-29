-- 订单表增加起点终点经伟度 ORACLE 47.98.39.45 已经操作成功 
-- Add/modify columns 
alter table CL_DD add DESTINATION_LNG VARCHAR2(32);
alter table CL_DD add DESTINATION_LAT VARCHAR2(32);
alter table CL_DD add ORIGIN_LNG VARCHAR2(32);
alter table CL_DD add ORIGIN_LAT VARCHAR2(32);
-- Add comments to the columns
comment on column CL_DD.DESTINATION_LNG
  is '结束点经度';
comment on column CL_DD.DESTINATION_LAT
  is '结束点纬度';
comment on column CL_DD.ORIGIN_LNG
  is '起始经度';
comment on column CL_DD.ORIGIN_LAT
  is '起始纬度';
