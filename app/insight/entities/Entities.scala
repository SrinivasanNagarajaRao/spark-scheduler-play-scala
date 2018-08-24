package insight.entities

case class User(id: Long, firstName: String, lastName: String, mobile: Long, email: String)

case class PreviewData(preview_data: List[Map[String, String]], schema_list: List[String])

case class DemoResponse(demograph_data: List[Map[String, List[String]]])

case class PreviewDataSample(preview_data: Array[Map[String, String]])

case class ParallelEntity(id: Option[Long], Group: Boolean, Group_Name: String, Variable: List[String], config_name: String, Cluster: String, PCA: String)

case class ParallelEntityunclus(id: Option[Long], Group: Boolean, Group_Name: String, Variable: List[String], config_name: String)

case class scatterEntity(Value: Double, Mean: Double, SD: Double)

case class scatterEntityfINAL(ID: String, list: List[scatterEntity])

case class PostEntity(id: Option[Long], country_code: List[String], motor: List[String], series: List[String],
                      Fuel: List[String], transmission: List[String], displacement: List[String], drive: List[String], hybrid_0_1: List[String], mileage: List[String],
                      car_age: List[String], plant_name: List[String], dealer_name: List[String], KIEFA: List[String], Defect_Code_06: List[String],
                      Defect_Code_08: List[String], Overlay_VIN: List[String], config_name: String, FromDate: String, ToDate: String, Variable_Group: Option[List[ParallelEntity]])

case class PostEntity2(id: Option[Long], country_code: List[String], motor: List[String], series: List[String],
                       Fuel: List[String], transmission: List[String], displacement: List[String], drive: List[String], hybrid_0_1: List[String], mileage: List[String],
                       car_age: List[String], plant_name: List[String], dealer_name: List[String], KIEFA: List[String], Defect_Code_06: List[String],
                       Defect_Code_08: List[String], Overlay_VIN: List[String], config_name: String, FromDate: String, ToDate: String, Variable_Group: Option[List[ParallelEntityunclus]])


case class FilterEntity(id: Option[Long], variable: String, config_name: String)

case class DemographicEntity(id: Option[Long], variable: List[String], config_name: String)

case class GroupEntity(id: Option[Long], Group: Boolean, Group_Name: String, Variable: List[String], config_name: String)

case class ProfileEntity(id: Option[Long], Random_VIN: String)

case class MapEntity(id: Option[Long], config_name: String)

case class LoadEntity(id: Option[Long], config_name: String)

case class Input(Type: String, config: String)

case class statusQuery(hashDb: String, redisKey: String)

case class SpiderEntity(id: Option[Long], config_name: List[String])

case class SpiderDefectEntity(id: Option[Long], config_name: List[String])

case class OverlayEntity(config_name: List[String])

case class ListOfMap(key: Map[String, List[Map[String, Double]]])

case class heatMapEntity(key: List[Map[String, String]])

case class HeatMapData(id: Option[Long], config_name: String)

case class DeleteConfigData(config_name: List[String])

case class NearbyVin(primaryConfig: String, secondaryConfig: String, datalimit: Int)

case class GlobalFilters(id: Option[Long], country_code: List[String], motor: List[String], series: List[String],
                         Fuel: List[String], transmission: List[String], displacement: List[String], drive: List[String], hybrid_0_1: List[String], mileage: List[String],
                         car_age: List[String], plant_name: List[String], dealer_name: List[String], KIEFA: List[String], Defect_Code_06: List[String],
                         Defect_Code_08: List[String], config_name: String, Variable_Group: List[ParallelEntityunclus])

case class CustomSplit(featureIndex: Int, leftCategoriesOrThreshold: List[Double], numCategories: Int)

case class ClassDetailsPerNode(node: String, query: String, label: String, percentage: Double, rate: Double)

case class RegDetailsPerNode(mean: Double, median: Double)

case class DatasetResult(num_of_rows: Long, num_of_cols: Int)

case class Node(id: Int, prediction: Double, impurity: Double, impurityStats: List[Double],
                gain: Double, leftChild: Int, rightChild: Int, split: CustomSplit,
                node_instances: Option[Long], split_condition: Option[String],
                node_instances_per_class: Option[List[ClassDetailsPerNode]],
                reg_node_details: Option[RegDetailsPerNode],
                connector_length: Option[Double])