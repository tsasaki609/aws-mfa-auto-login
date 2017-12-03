# ログインしていない状態で実行してください

# 設定
$url = "Your_ConsoleLoginURL"
$username="Your_UserName"
$password="Your_Password"
$etOtpPassword="Your_etOTPPassword"

$ie = New-Object -ComObject InternetExplorer.Application
$ie.Visible = $true
$ie.Navigate($url)

While($ie.Busy){ Start-Sleep -s 1 }

# 1段階目
$doc = $ie.Document
[System.__ComObject].InvokeMember("getElementById",[System.Reflection.BindingFlags]::InvokeMethod, $null, $doc, @( "username" )).value = $username
[System.__ComObject].InvokeMember("getElementById",[System.Reflection.BindingFlags]::InvokeMethod, $null, $doc, @( "password" )).value = $password
[System.__ComObject].InvokeMember("getElementById",[System.Reflection.BindingFlags]::InvokeMethod, $null, $doc, @( "signin_button" )).Click()

While($ie.Busy){ Start-Sleep -s 1 }

# 2段階目
$mfacode = java -cp * xyz.easy_coding.MultiFacotorAuthCode $etOtpPassword
[System.__ComObject].InvokeMember("getElementById",[System.Reflection.BindingFlags]::InvokeMethod, $null, $doc, @( "mfacode" )).value = $mfacode
[System.__ComObject].InvokeMember("getElementById",[System.Reflection.BindingFlags]::InvokeMethod, $null, $doc, @( "submitMfa_button" )).Click()
