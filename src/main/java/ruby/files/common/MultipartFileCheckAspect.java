package ruby.files.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.exception.InvalidFileTypeException;
import ruby.files.common.exception.InvalidFilenameException;

import java.util.Arrays;

@Aspect
@Component
public class MultipartFileCheckAspect {

    @Around("@annotation(ruby.files.common.MultipartFileCheck)")
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        MultipartFileCheck multipartFileCheck = methodSignature.getMethod().getAnnotation(MultipartFileCheck.class);
        MultipartFileType[] multipartFileTypes = multipartFileCheck.checkTypes();
        Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterTypes.length; i++) {
            if (!isMultipartFile(parameterTypes[i])) {
                continue;
            }

            MultipartFile multipartFile = (MultipartFile) args[i];
            if (!isValidType(multipartFile, multipartFileTypes)) {
                throw new InvalidFileTypeException();
            }

            if (!isValidFilename(multipartFile)) {
                throw new InvalidFilenameException();
            }
        }

        return joinPoint.proceed();
    }

    private boolean isMultipartFile(Class<?> classType) {
        return classType.equals(MultipartFile.class);
    }

    private boolean isValidType(MultipartFile multipartFile, MultipartFileType[] multipartFileTypes) {
        return Arrays.stream(multipartFileTypes)
            .anyMatch(multipartFileType -> multipartFileType.isType(multipartFile));
    }

    private boolean isValidFilename(MultipartFile multipartFile) {
        // 조건이 필요할 경우 추가
        String originalFilename = multipartFile.getOriginalFilename();
        return originalFilename != null && !originalFilename.isEmpty();
    }
}
