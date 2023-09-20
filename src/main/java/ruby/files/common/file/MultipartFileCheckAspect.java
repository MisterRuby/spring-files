package ruby.files.common.file;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.file.exception.InvalidFileTypeException;
import ruby.files.common.file.exception.InvalidFilenameException;

import java.util.List;

@Aspect
@Component
public class MultipartFileCheckAspect {

    @Around("@annotation(ruby.files.common.file.MultipartFileCheck)")
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        MultipartFileCheck multipartFileCheck = methodSignature.getMethod().getAnnotation(MultipartFileCheck.class);
        MultipartFileType multipartFileType = multipartFileCheck.checkType();
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (isMultipartFile(arg)) {
                MultipartFile multipartFile = (MultipartFile) arg;
                checkMultipartFileException(multipartFile, multipartFileType);
            } else if (isMultipartMultipleFile(arg)) {
                ((List<?>)arg).forEach(multipartFile ->
                    checkMultipartFileException((MultipartFile) multipartFile, multipartFileType)
                );
            }
        }

        return joinPoint.proceed();
    }

    private boolean isMultipartFile(Object arg) {
        return arg instanceof MultipartFile;
    }

    private boolean isMultipartMultipleFile(Object arg) {
        if (!(arg instanceof List<?>)) {
            return false;
        }

        return ((List<?>) arg).stream().allMatch(this::isMultipartFile);
    }

    private void checkMultipartFileException(MultipartFile multipartFile, MultipartFileType multipartFileType) {
        if (!multipartFileType.isValidType(multipartFile)) {
            throw new InvalidFileTypeException();
        }

        if (!isValidFilename(multipartFile)) {
            throw new InvalidFilenameException();
        }
    }

    private boolean isValidFilename(MultipartFile multipartFile) {
        // 조건이 필요할 경우 추가
        String originalFilename = multipartFile.getOriginalFilename();
        return originalFilename != null && !originalFilename.isEmpty();
    }
}
