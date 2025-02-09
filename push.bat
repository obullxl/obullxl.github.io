@echo off
:loop
echo Attempting to push changes...
git push

REM Check the error level after git push
if %ERRORLEVEL% neq 0 (
    echo Git push failed, trying again in 1 second.
    echo --------------------------------------------
    timeout /t 1 >nul
    goto loop
) else (
    echo Git push succeeded.
    goto end
)

:end
echo Script completed.
echo =========================
echo Git push 代码成功
echo =========================