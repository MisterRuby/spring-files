package ruby.files.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.exception.InvalidFileTypeException;
import ruby.files.common.exception.InvalidFilenameException;

@Aspect
@Component
public class MultipartFileCheckAspect {

    @Around("@annotation(ruby.files.common.MultipartFileCheck)")
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        MultipartFileCheck multipartFileCheck = methodSignature.getMethod().getAnnotation(MultipartFileCheck.class);
        MultipartFileType multipartFileType = multipartFileCheck.checkType();
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (!isMultipartFile(arg)) {
                continue;
            }

            MultipartFile multipartFile = (MultipartFile) arg;
            if (!multipartFileType.isValidType(multipartFile)) {
                throw new InvalidFileTypeException();
            }

            if (!isValidFilename(multipartFile)) {
                throw new InvalidFilenameException();
            }
        }

        return joinPoint.proceed();
    }

    private boolean isMultipartFile(Object arg) {
        return arg instanceof MultipartFile;
    }

    private boolean isValidFilename(MultipartFile multipartFile) {
        // 조건이 필요할 경우 추가
        String originalFilename = multipartFile.getOriginalFilename();
        return originalFilename != null && !originalFilename.isEmpty();
    }
}
