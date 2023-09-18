package ruby.files.image;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.image.exception.ImageTypeException;

import java.util.Objects;

@Aspect
@Component
public class ImageAspect {

    @Around("@annotation(ruby.files.image.ImageCheck)")
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterTypes.length; i++) {
            if (!isMultipartFile(parameterTypes[i])) {
                continue;
            }

            MultipartFile image = (MultipartFile) args[i];
            if (isImage(image)) {
                return joinPoint.proceed();
            }
        }

        throw new ImageTypeException();
    }

    private boolean isMultipartFile(Class<?> classType) {
        return classType.equals(MultipartFile.class);
    }

    private boolean isImage(MultipartFile image) {
        return Objects.requireNonNull(image.getContentType()).toLowerCase().startsWith("image");
    }
}
