cl{{- define "account-service.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "account-service.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}

{{- define "account-service.selectorLabels" -}}
app: {{ include "blocker-service.name" . }}
{{- end }}

{{- define "account-service.labels" -}}
app: {{ include "account-service.name" . }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
release: {{ .Release.Name }}
heritage: {{ .Release.Service }}
{{- end }}