package insight.controllers

import insight.entities._
import play.api.libs.json._

object ControllerImplicits {

  implicit val User_writes = Json.writes[User]
  implicit val User_reads = Json.reads[User]

  implicit val Input_writes = Json.writes[Input]
  implicit val Input_reads = Json.reads[Input]

  implicit val Status_writes = Json.writes[statusQuery]
  implicit val Status_reads = Json.reads[statusQuery]

  implicit val preview_data_writes = Json.writes[PreviewData]
  implicit val preview_data_reads = Json.reads[PreviewData]

  implicit val parallel_entity_write = Json.writes[ParallelEntity]
  implicit val parallel_entity_read = Json.reads[ParallelEntity]

  implicit val parallelun_entity_write = Json.writes[ParallelEntityunclus]
  implicit val parallelun_entity_read = Json.reads[ParallelEntityunclus]

  implicit val post_entity_write = Json.writes[PostEntity]
  implicit val post_entity_read = Json.reads[PostEntity]

  implicit val post2_entity_write = Json.writes[PostEntity2]
  implicit val post2_entity_read = Json.reads[PostEntity2]

  implicit val filter_entity_write = Json.writes[FilterEntity]
  implicit val filter_entity_read = Json.reads[FilterEntity]

  implicit val demographic_entity_write = Json.writes[DemographicEntity]
  implicit val demographic_entity_read = Json.reads[DemographicEntity]

  implicit val demographic_res_write = Json.writes[DemoResponse]
  implicit val demographic_res_read = Json.reads[DemoResponse]

  implicit val Profile_entity_write = Json.writes[ProfileEntity]
  implicit val Profile_entity_read = Json.reads[ProfileEntity]

  implicit val map_entity_write = Json.writes[MapEntity]
  implicit val map_entity_read = Json.reads[MapEntity]

  implicit val load_entity_write = Json.writes[LoadEntity]
  implicit val load_entity_read = Json.reads[LoadEntity]

  implicit val group_entity_write = Json.writes[GroupEntity]
  implicit val group_entity_read = Json.reads[GroupEntity]

  implicit val spider_entity_write = Json.writes[SpiderEntity]
  implicit val spider_entity_read = Json.reads[SpiderEntity]

  implicit val spider_defectentity_write = Json.writes[SpiderDefectEntity]
  implicit val spider_defectentity_read = Json.reads[SpiderDefectEntity]

  implicit val ListOfMapOfList_write = Json.writes[ListOfMap]
  implicit val ListOfMapOfList_read = Json.reads[ListOfMap]

  implicit val heatMapEntity_write = Json.writes[heatMapEntity]
  implicit val heatMapEntity_read = Json.reads[heatMapEntity]

  implicit val HeatMapData_write = Json.writes[HeatMapData]
  implicit val HeatMapData_read = Json.reads[HeatMapData]

  implicit val overlay_entity_write = Json.writes[OverlayEntity]
  implicit val overlay_entity_read = Json.reads[OverlayEntity]

  implicit val delete_entity_write = Json.writes[DeleteConfigData]
  implicit val delete_entity_read = Json.reads[DeleteConfigData]

  implicit val NearbyVin_write = Json.writes[NearbyVin]
  implicit val NearbyVin_read = Json.reads[NearbyVin]

  implicit val Global_Filters_write = Json.writes[GlobalFilters]
  implicit val Global_Filters_read = Json.reads[GlobalFilters]

  implicit val split_writes = Json.writes[CustomSplit]
  implicit val split_reads = Json.reads[CustomSplit]

  implicit val class_details_per_node_writes = Json.writes[ClassDetailsPerNode]
  implicit val class_details_per_node_reads = Json.reads[ClassDetailsPerNode]

  implicit val reg_details_per_node_writes = Json.writes[RegDetailsPerNode]
  implicit val reg_details_per_node_reads = Json.reads[RegDetailsPerNode]

  implicit val node_writes = Json.writes[Node]
  implicit val node_reads = Json.reads[Node]

  implicit val dataset_writes = Json.writes[DatasetResult]
  implicit val dataset_reads = Json.reads[DatasetResult]

}
