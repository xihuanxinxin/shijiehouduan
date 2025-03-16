Write-Host "开始清理不需要的文件..." -ForegroundColor Green

# 删除不需要的控制器
Write-Host "删除不需要的控制器..." -ForegroundColor Yellow
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\AdminController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\AppointmentController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\BedController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\EyeExaminationController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\HospitalizationController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\MedicalRecordController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\PatientController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\PaymentApiController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\PaymentController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\SurgeryController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\SystemConfigController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\SystemLogController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\TestController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\UserController.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\controller\UserManagementController.java" -ErrorAction SilentlyContinue

# 删除不需要的服务
Write-Host "删除不需要的服务..." -ForegroundColor Yellow
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\AdministratorService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\AppointmentService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\BedService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\DoctorService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\EyeExaminationService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\HospitalizationService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\MedicalRecordService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\PaymentService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\SecurityService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\SurgeryService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\SystemConfigService.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\SystemLogService.java" -ErrorAction SilentlyContinue

# 删除不需要的服务实现类
Write-Host "删除不需要的服务实现类..." -ForegroundColor Yellow
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\AdministratorServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\AppointmentServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\BedServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\DoctorServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\EyeExaminationServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\HospitalizationServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\MedicalRecordServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\PaymentServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\SecurityServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\SurgeryServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\SystemConfigServiceImpl.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\service\impl\SystemLogServiceImpl.java" -ErrorAction SilentlyContinue

# 删除不需要的DAO
Write-Host "删除不需要的DAO..." -ForegroundColor Yellow
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\AdministratorDao.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\AppointmentDao.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\BedDao.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\DoctorDao.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\EyeExaminationDao.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\HospitalizationDao.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\MedicalRecordDao.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\PaymentDao.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\SurgeryDao.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\SystemConfigDao.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\dao\SystemLogDao.java" -ErrorAction SilentlyContinue

# 删除不需要的实体类
Write-Host "删除不需要的实体类..." -ForegroundColor Yellow
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\Administrator.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\Appointment.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\Bed.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\DatabaseBackup.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\Doctor.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\EyeExamination.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\Hospitalization.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\MedicalRecord.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\Medicine.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\Payment.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\Prescription.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\PrescriptionMedicine.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\Surgery.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\SystemConfig.java" -ErrorAction SilentlyContinue
Remove-Item -Path "src\main\java\com\example\shijiehouduan\entity\SystemLog.java" -ErrorAction SilentlyContinue

# 删除不需要的文档
Write-Host "删除不需要的文档..." -ForegroundColor Yellow
Remove-Item -Path "移除功能计划.md" -ErrorAction SilentlyContinue
Remove-Item -Path "cleanup.bat" -ErrorAction SilentlyContinue

Write-Host "清理完成！" -ForegroundColor Green 