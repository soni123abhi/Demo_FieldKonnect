data class FillterModel(
    val status: String,
    val data: List<FieldData>
)

data class FieldData(
    val id: Int,
    val field_name: String,
    val key: String,
    val field_type: String,
    val input_type: String,
    val fields_data: List<FieldOption>
)

data class FieldOption(
    val id: Int,
    val value: String,
    val field_id: Int
)
