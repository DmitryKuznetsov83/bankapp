cl{{- define "transfer-service.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "transfer-service.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}

{{- define "transfer-service.selectorLabels" -}}
app: {{ include "cash-service.name" . }}
{{- end }}

{{- define "transfer-service.labels" -}}
app: {{ include "cash-service.name" . }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
release: {{ .Release.Name }}
heritage: {{ .Release.Service }}
{{- end }}