CREATE DEFINER=`root`@`localhost` PROCEDURE `get_variable`(IN variableId INT)
BEGIN

SELECT V.`id` as id,
    V.`device_id` as deviceId,
    V.`name` as name,
    V.`description` as description,
    V.`unit_id` as unitID,
    U.code as unitCode,
    V.`updating_duration` as updatingDuration,
    V.`set_command_id` as setCommandID,
    V.`get_command_id` as getCommandID
FROM `smes_microgrid`.variable AS V
LEFT JOIN `smes_microgrid`.variable_unit AS U ON U.id = V.unit_id
WHERE V.id = variableId
;

END