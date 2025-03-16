@echo off
echo 开始清理不需要的文件...

REM 删除不需要的控制器
echo 删除不需要的控制器...
del /q "src\main\java\com\example\shijiehouduan\controller\AdminController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\AppointmentController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\BedController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\EyeExaminationController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\HospitalizationController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\MedicalRecordController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\PatientController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\PaymentApiController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\PaymentController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\SurgeryController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\SystemConfigController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\SystemLogController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\TestController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\UserController.java"
del /q "src\main\java\com\example\shijiehouduan\controller\UserManagementController.java"

REM 删除不需要的服务
echo 删除不需要的服务...
del /q "src\main\java\com\example\shijiehouduan\service\AdministratorService.java"
del /q "src\main\java\com\example\shijiehouduan\service\AppointmentService.java"
del /q "src\main\java\com\example\shijiehouduan\service\BedService.java"
del /q "src\main\java\com\example\shijiehouduan\service\DoctorService.java"
del /q "src\main\java\com\example\shijiehouduan\service\EyeExaminationService.java"
del /q "src\main\java\com\example\shijiehouduan\service\HospitalizationService.java"
del /q "src\main\java\com\example\shijiehouduan\service\MedicalRecordService.java"
del /q "src\main\java\com\example\shijiehouduan\service\PaymentService.java"
del /q "src\main\java\com\example\shijiehouduan\service\SecurityService.java"
del /q "src\main\java\com\example\shijiehouduan\service\SurgeryService.java"
del /q "src\main\java\com\example\shijiehouduan\service\SystemConfigService.java"
del /q "src\main\java\com\example\shijiehouduan\service\SystemLogService.java"

REM 删除不需要的服务实现类
echo 删除不需要的服务实现类...
del /q "src\main\java\com\example\shijiehouduan\service\impl\AdministratorServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\AppointmentServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\BedServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\DoctorServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\EyeExaminationServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\HospitalizationServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\MedicalRecordServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\PaymentServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\SecurityServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\SurgeryServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\SystemConfigServiceImpl.java"
del /q "src\main\java\com\example\shijiehouduan\service\impl\SystemLogServiceImpl.java"

REM 删除不需要的DAO
echo 删除不需要的DAO...
del /q "src\main\java\com\example\shijiehouduan\dao\AdministratorDao.java"
del /q "src\main\java\com\example\shijiehouduan\dao\AppointmentDao.java"
del /q "src\main\java\com\example\shijiehouduan\dao\BedDao.java"
del /q "src\main\java\com\example\shijiehouduan\dao\DoctorDao.java"
del /q "src\main\java\com\example\shijiehouduan\dao\EyeExaminationDao.java"
del /q "src\main\java\com\example\shijiehouduan\dao\HospitalizationDao.java"
del /q "src\main\java\com\example\shijiehouduan\dao\MedicalRecordDao.java"
del /q "src\main\java\com\example\shijiehouduan\dao\PaymentDao.java"
del /q "src\main\java\com\example\shijiehouduan\dao\SurgeryDao.java"
del /q "src\main\java\com\example\shijiehouduan\dao\SystemConfigDao.java"
del /q "src\main\java\com\example\shijiehouduan\dao\SystemLogDao.java"

REM 删除不需要的实体类
echo 删除不需要的实体类...
del /q "src\main\java\com\example\shijiehouduan\entity\Administrator.java"
del /q "src\main\java\com\example\shijiehouduan\entity\Appointment.java"
del /q "src\main\java\com\example\shijiehouduan\entity\Bed.java"
del /q "src\main\java\com\example\shijiehouduan\entity\DatabaseBackup.java"
del /q "src\main\java\com\example\shijiehouduan\entity\Doctor.java"
del /q "src\main\java\com\example\shijiehouduan\entity\EyeExamination.java"
del /q "src\main\java\com\example\shijiehouduan\entity\Hospitalization.java"
del /q "src\main\java\com\example\shijiehouduan\entity\MedicalRecord.java"
del /q "src\main\java\com\example\shijiehouduan\entity\Medicine.java"
del /q "src\main\java\com\example\shijiehouduan\entity\Payment.java"
del /q "src\main\java\com\example\shijiehouduan\entity\Prescription.java"
del /q "src\main\java\com\example\shijiehouduan\entity\PrescriptionMedicine.java"
del /q "src\main\java\com\example\shijiehouduan\entity\Surgery.java"
del /q "src\main\java\com\example\shijiehouduan\entity\SystemConfig.java"
del /q "src\main\java\com\example\shijiehouduan\entity\SystemLog.java"

echo 清理完成！ 